import { defineStore } from 'pinia'
import { login as loginApi, logout as logoutApi } from '@/api/auth'
import { markAuthenticated, clearAuthentication } from '@/utils/auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    username: sessionStorage.getItem('username') || '',
    realName: sessionStorage.getItem('realName') || '',
    permissions: JSON.parse(sessionStorage.getItem('permissions') || '[]') as string[]
  }),
  actions: {
    async login(form: any) {
      const res: any = await loginApi(form)
      markAuthenticated()
      this.username = res.data.username
      this.realName = res.data.realName
      this.permissions = res.data.permissions || []
      sessionStorage.setItem('username', this.username)
      sessionStorage.setItem('realName', this.realName)
      sessionStorage.setItem('permissions', JSON.stringify(this.permissions))
    },
    async logout() {
      try { await logoutApi() } finally {
        clearAuthentication()
        sessionStorage.clear()
        this.username = ''
        this.realName = ''
        this.permissions = []
      }
    }
  }
})
