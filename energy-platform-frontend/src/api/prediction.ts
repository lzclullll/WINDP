import request from '@/utils/request'
export const predictionHistory=(p:any)=>request.get('/predict/history',{params:p})
export const predictionTasks=(p:any)=>request.get('/predict/tasks',{params:p})
export const createPredictionTask=(d:any)=>request.post('/predict/tasks',d)
export const runPredictionTask=(id:number)=>request.post('/predict/tasks/'+id+'/run')
export const deletePredictionTask=(id:number)=>request.delete('/predict/tasks/'+id)
