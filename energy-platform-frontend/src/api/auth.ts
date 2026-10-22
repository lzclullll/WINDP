import request from '@/utils/request'
export const login = (data:any) => request.post('/auth/login', data)
export const logout = () => request.post('/auth/logout')
export const refresh = () => request.post('/auth/refresh')