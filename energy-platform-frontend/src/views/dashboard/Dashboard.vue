<template>
  <div class="page">
    <div class="grid">
      <div v-for="item in kpis" :key="item.label" class="panel kpi">
        <span>{{ item.label }}</span><b>{{ item.value }}</b>
      </div>
    </div>

    <div class="layout">
      <div class="panel">
        <div class="chart-heading">
          <strong>功率预测趋势</strong>
        </div>
        <BaseChart :option="lineOption" />
      </div>
      <div class="panel">
        <div class="chart-heading">
          <strong>电站运行状态</strong>
        </div>
        <BaseChart :option="pieOption" />
      </div>
    </div>

    <div class="layout">
      <div class="panel">
        <div class="chart-heading">
          <strong>各电站当前功率对比</strong>
        </div>
        <BaseChart :option="barOption" />
      </div>
      <el-card>
        <template #header>最近告警</template>
        <el-table :data="alarms">
          <el-table-column prop="title" label="标题" />
          <el-table-column prop="alarmLevel" label="等级" />
          <el-table-column prop="status" label="状态" />
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import BaseChart from '@/components/charts/BaseChart.vue'
import { powerStatistics } from '@/api/power'
import { predictionHistory } from '@/api/prediction'
import { listAlarms } from '@/api/alarm'
import { useAuthStore } from '@/stores/auth'

const stats = ref<any>({})
const alarms = ref<any[]>([])
const predictionPoints = ref<any[]>([])
const auth = useAuthStore()
let timer: number | undefined

const loadOverview = async () => {
  stats.value = (await powerStatistics() as any).data
  alarms.value = auth.permissions.includes('alarm:manage')
    ? (await listAlarms({ page: 1, size: 5 }) as any).data.records
    : []
}

const loadPrediction = async () => {
  if (!auth.permissions.includes('prediction:manage')) return
  const response: any = await predictionHistory({ page: 1, size: 1 })
  const latest = response.data.records?.[0]
  if (!latest?.resultData) return
  try {
    const parsed = typeof latest.resultData === 'string' ? JSON.parse(latest.resultData) : latest.resultData
    predictionPoints.value = Array.isArray(parsed) ? parsed : []
  } catch {
    predictionPoints.value = []
  }
}

onMounted(() => {
  loadOverview()
  loadPrediction()
  timer = window.setInterval(loadOverview, 15000)
})

onUnmounted(() => {
  if (timer) window.clearInterval(timer)
})

const kpis = computed(() => [
  { label: '全站实时功率', value: (stats.value.totalPower || 0).toLocaleString() + ' kW' },
  { label: '当日发电量', value: (stats.value.todayGeneration || 0).toLocaleString() + ' kWh' },
  { label: '电站数量', value: Number(stats.value.stationCount || 0).toLocaleString() },
  { label: '告警数量', value: Number(stats.value.alarmCount || 0).toLocaleString() },
  { label: '工单数量', value: Number(stats.value.workOrderCount || 0).toLocaleString() }
])

const lineOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: predictionPoints.value.map(point => String(point.time).slice(11, 16)) },
  yAxis: { type: 'value', name: '预测功率(kW)' },
  series: [{ name: '预测功率', type: 'line', smooth: true, data: predictionPoints.value.map(point => point.power) }]
}))

const pieOption = computed(() => {
  const status = stats.value.stationStatus || {}
  return {
    tooltip: { trigger: 'item', formatter: '{b}：{c} 个（{d}%）' },
    legend: { bottom: 0 },
    series: [{
      name: '电站状态',
      type: 'pie',
      radius: ['38%', '68%'],
      data: [
        { name: '运行中', value: Number(status.RUNNING || 0) },
        { name: '维护中', value: Number(status.MAINTENANCE || 0) },
        { name: '已停用', value: Number(status.STOPPED || 0) }
      ]
    }]
  }
})

const barOption = computed(() => {
  const stations = stats.value.stationPowers || []
  return {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' }, valueFormatter: (value: unknown) => `${value} kW` },
    xAxis: { type: 'category', name: '电站', data: stations.map((station: any) => station.stationName), axisLabel: { interval: 0, rotate: stations.length > 5 ? 20 : 0 } },
    yAxis: { type: 'value', name: '当前功率(kW)' },
    series: [{ name: '最新功率', type: 'bar', data: stations.map((station: any) => station.powerKw), barMaxWidth: 46 }]
  }
})
</script>

<style scoped>
.layout { display: grid; grid-template-columns: 2fr 1fr; gap: 16px; }
.chart-heading { margin-bottom: 8px; }
</style>
