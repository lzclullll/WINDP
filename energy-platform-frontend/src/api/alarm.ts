import request from '@/utils/request'
export const listAlarms=(p:any)=>request.get('/alarms',{params:p})
export const alarmStats=()=>request.get('/alarms/statistics')
export const handleAlarm=(id:number)=>request.post(`/alarms/${id}/handle`)
export const createWorkOrderFromAlarm=(id:number)=>request.post(`/alarms/${id}/create-work-order`)
export const deleteAlarm=(id:number)=>request.delete('/alarms/'+id)
