import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({ baseURL: '/api', timeout: 15000, withCredentials: true })
const refreshClient = axios.create({ baseURL: '/api', timeout: 15000, withCredentials: true })
let refreshPromise: Promise<void> | null = null

const refreshSession = () => {
  if (!refreshPromise) {
    refreshPromise = refreshClient.post('/auth/refresh')
      .then(() => undefined)
      .finally(() => { refreshPromise = null })
  }
  return refreshPromise
}

const returnToLogin = () => {
  sessionStorage.clear()
  if (location.hash !== '#/login') location.replace('/#/login')
}

request.interceptors.response.use(response => {
  const payload = response.data
  if (payload && typeof payload.code === 'number' && payload.code >= 400) {
    ElMessage.error(payload.message || '请求失败')
    return Promise.reject(new Error(payload.message || '请求失败'))
  }
  return payload
}, async error => {
  const status = error.response?.status
  const originalRequest = error.config as (typeof error.config & { _retry?: boolean })
  const isLoginRequest = originalRequest?.url?.includes('/auth/login')
  const isRefreshRequest = originalRequest?.url?.includes('/auth/refresh')

  if (status === 401 && originalRequest && !originalRequest._retry && !isLoginRequest && !isRefreshRequest) {
    originalRequest._retry = true
    try {
      await refreshSession()
      return request(originalRequest)
    } catch {
      ElMessage.error('登录已过期，请重新登录')
      returnToLogin()
      return Promise.reject(error)
    }
  }

  if (status === 401 && !isLoginRequest) returnToLogin()
  ElMessage.error(error.response?.data?.message || error.message || '请求失败')
  return Promise.reject(error)
})

export default request
