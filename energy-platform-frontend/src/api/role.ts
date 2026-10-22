import request from '@/utils/request'
export const listRoles=(p:any)=>request.get('/roles',{params:p})