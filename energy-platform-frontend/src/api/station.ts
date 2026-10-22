import request from '@/utils/request'
export const listStations=(p:any)=>request.get('/stations',{params:p})
export const stationDevices=(id:number)=>request.get('/stations/'+id+'/devices')
export const saveStation=(d:any)=>request.post('/stations',d)
export const updateStation=(id:number,d:any)=>request.put('/stations/'+id,d)
export const deleteStation=(id:number)=>request.delete('/stations/'+id)
