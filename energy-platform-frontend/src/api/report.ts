import request from '@/utils/request'
export const dailyReport=()=>request.get('/reports/daily')
export const monthlyReport=()=>request.get('/reports/monthly')
export const yearlyReport=()=>request.get('/reports/yearly')