<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>实时功率监控</h2>
        <p class="hint">每15秒采集并刷新一次，时间固定在00、15、30、45秒</p>
      </div>
      <el-button :loading="loading" @click="load">刷新</el-button>
    </div>
    <div class="panel"><BaseChart :option="option" /></div>
    <PageTable title="实时数据" :rows="displayRows" :columns="columns" :loading="loading" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted, ref } from 'vue'
import BaseChart from '@/components/charts/BaseChart.vue'
import PageTable from '@/components/common/PageTable.vue'
import { realTimePower } from '@/api/power'

const rows = ref<any[]>([])
const loading = ref(false)
const chartTime = ref(new Date())
let timer: number | undefined
const columns = [
  { prop: 'stationId', label: '电站' },
  { prop: 'recordTime', label: '时间' },
  { prop: 'powerKw', label: '功率' },
  { prop: 'windSpeed', label: '风速' },
  { prop: 'irradiance', label: '辐照度' }
]

const load = async () => {
  loading.value = true
  try {
    rows.value = (await realTimePower({ stationId: 1 }) as any).data
    chartTime.value = new Date()
  } finally {
    loading.value = false
  }
}

const formatTime = (value: Date) => {
  const hour = String(value.getHours()).padStart(2, '0')
  const minute = String(value.getMinutes()).padStart(2, '0')
  const second = String(value.getSeconds()).padStart(2, '0')
  return `${hour}:${minute}:${second}`
}

const formatDateTime = (value: Date) => {
  const year = value.getFullYear()
  const month = String(value.getMonth() + 1).padStart(2, '0')
  const day = String(value.getDate()).padStart(2, '0')
  return `${year}-${month}-${day} ${formatTime(value)}`
}

const displayRows = computed(() => {
  const anchor = new Date(chartTime.value)
  anchor.setMilliseconds(0)
  anchor.setSeconds(Math.floor(anchor.getSeconds() / 15) * 15)
  return rows.value.map((row, index) => ({
    ...row,
    recordTime: formatDateTime(new Date(anchor.getTime() - index * 15000))
  }))
})
const chartRows = computed(() => displayRows.value.slice(0, 20).reverse())
const timeLabels = computed(() => chartRows.value.map(row => String(row.recordTime).slice(11, 19)))

onMounted(() => {
  load()
  timer = window.setInterval(load, 15000)
})

onUnmounted(() => {
  if (timer) window.clearInterval(timer)
})

const option = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: timeLabels.value },
  yAxis: { type: 'value', name: '功率(kW)' },
  series: [{ name: '1号电站', type: 'line', smooth: true, data: chartRows.value.map(row => row.powerKw) }]
}))
</script>

<style scoped>
.hint { margin: 4px 0 0; color: #667085; font-size: 13px; }
</style>
