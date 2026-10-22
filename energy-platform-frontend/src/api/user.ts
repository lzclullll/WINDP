import request from '@/utils/request'
export const listUsers=(p:any)=>request.get('/users',{params:p})
export const saveUser=(d:any)=>request.post('/users',d)
export const updateUser=(id:number,d:any)=>request.put('/users/'+id,d)
export const deleteUser=(id:number)=>request.delete('/users/'+id)
