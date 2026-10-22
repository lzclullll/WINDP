import { useAuthStore } from '@/stores/auth'
export const usePermission=()=>({ has:(p:string)=>useAuthStore().permissions.includes(p) || useAuthStore().permissions.includes('system:manage') })