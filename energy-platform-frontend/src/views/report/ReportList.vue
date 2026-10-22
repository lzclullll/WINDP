<template>
  <div class="page">
    <div class="toolbar"><h2>报表管理</h2><el-button :loading="loading" @click="load">刷新报表</el-button></div>
    <el-card v-loading="loading" class="report-section">
      <template #header><strong>今日运行概况</strong></template>
      <div class="metric-grid">
        <div class="metric"><span>日发电量</span><b>{{ number(daily.generation) }}</b><small>kWh</small></div>
        <div class="metric"><span>告警总数</span><b>{{ number(daily.alarms) }}</b><small>条</small></div>
        <div class="metric"><span>待处理告警</span><b>{{ number(daily.openAlarms) }}</b><small>条</small></div>
        <div class="metric"><span>工单总数</span><b>{{ number(daily.orders) }}</b><small>张</small></div>
        <div class="metric"><span>进行中工单</span><b>{{ number(daily.activeOrders) }}</b><small>张</small></div>
        <div class="metric"><span>电站数量</span><b>{{ number(daily.stations) }}</b><small>座</small></div>
      </div>
    </el-card>
    <el-card v-loading="loading" class="report-section">
      <template #header><strong>月度经营统计</strong></template>
      <div class="metric-grid">
        <div class="metric"><span>月发电量</span><b>{{ number(monthly.generation) }}</b><small>kWh</small></div>
        <div class="metric"><span>设备可用率</span><b>{{ number(monthly.availability) }}</b><small>%</small></div>
        <div class="metric"><span>运维成本</span><b>{{ money(monthly.cost) }}</b><small>元</small></div>
      </div>
    </el-card>
    <el-card v-loading="loading" class="report-section">
      <template #header><strong>年度运行统计</strong></template>
      <div class="metric-grid two-columns">
        <div class="metric"><span>年发电量</span><b>{{ number(yearly.generation) }}</b><small>kWh</small></div>
        <div class="metric"><span>设备可用率</span><b>{{ number(yearly.availability) }}</b><small>%</small></div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { dailyReport, monthlyReport, yearlyReport } from '@/api/report'

const daily = ref<any>({})
const monthly = ref<any>({})
const yearly = ref<any>({})
const loading = ref(false)
const number = (value: unknown) => value == null ? '--' : Number(value).toLocaleString('zh-CN')
const money = (value: unknown) => value == null ? '--' : Number(value).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
const load = async () => {
  loading.value = true
  try {
    const [dailyResponse, monthlyResponse, yearlyResponse] = await Promise.all([dailyReport(), monthlyReport(), yearlyReport()]) as any[]
    daily.value = dailyResponse.data
    monthly.value = monthlyResponse.data
    yearly.value = yearlyResponse.data
  } finally {
    loading.value = false
  }
}
onMounted(load)
</script>

<style scoped>
.report-section{margin-bottom:16px}.metric-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:16px}.metric{padding:18px;border-radius:10px;background:linear-gradient(135deg,#f5f9ff,#eef7f5);display:grid;grid-template-columns:1fr auto;align-items:end;gap:8px}.metric span{grid-column:1/-1;color:#667085}.metric b{font-size:26px;color:#16324f}.metric small{color:#667085;padding-bottom:4px}.two-columns{grid-template-columns:repeat(2,minmax(0,1fr))}@media(max-width:800px){.metric-grid,.two-columns{grid-template-columns:1fr}}
</style>
