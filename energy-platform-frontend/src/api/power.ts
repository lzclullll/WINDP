import request from '@/utils/request'
export const realTimePower=(p:any)=>request.get('/power/real-time',{params:p})
export const historyPower=(p:any)=>request.get('/power/history',{params:p})
export const powerStatistics=()=>request.get('/power/statistics')