import { defineStore } from 'pinia'
import { listStations } from '@/api/station'
export const useStationStore = defineStore('station', { state: () => ({ stations: [] as any[], currentId: 1 }), actions: { async load(){ const r:any=await listStations({page:1,size:100}); this.stations=r.data.records; if(this.stations[0]) this.currentId=this.stations[0].id } } })
