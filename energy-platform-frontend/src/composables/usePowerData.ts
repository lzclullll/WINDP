import { ref } from 'vue'
import { realTimePower } from '@/api/power'
export const usePowerData=()=>{ const rows=ref<any[]>([]); const load=async()=>{ const r:any=await realTimePower({}); rows.value=r.data }; return { rows, load } }