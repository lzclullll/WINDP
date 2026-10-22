import request from '@/utils/request'
export const listDevices=(p:any)=>request.get('/devices',{params:p})
export const saveDevice=(d:any)=>request.post('/devices',d)
export const updateDevice=(id:number,d:any)=>request.put('/devices/'+id,d)
export const deleteDevice=(id:number)=>request.delete('/devices/'+id)
