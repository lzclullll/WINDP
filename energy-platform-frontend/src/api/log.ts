import request from '@/utils/request'
export const operationLogs=(p:any)=>request.get('/logs/operation',{params:p})
export const loginLogs=(p:any)=>request.get('/logs/login',{params:p})