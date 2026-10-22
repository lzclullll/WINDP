import request from '@/utils/request'
export const listPermissions=(p:any)=>request.get('/permissions',{params:p})