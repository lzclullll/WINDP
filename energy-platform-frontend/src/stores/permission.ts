import { defineStore } from 'pinia'
export const usePermissionStore = defineStore('permission', { state: () => ({ menus: [] as any[] }) })
