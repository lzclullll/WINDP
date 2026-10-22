<template>
  <div class="page">
    <div class="toolbar">
      <div>
        <h2>预测结果分析</h2>
        <p class="hint">请在“预测任务”中运行任务，本页展示已完成的预测结果</p>
      </div>
      <el-button :loading="loading" @click="load">刷新结果</el-button>
    </div>

    <div v-if="chart.length" class="panel">
      <div class="chart-title">任务 #{{ selectedTaskId }} 的预测功率曲线</div>
      <BaseChart :option="option" />
    </div>
    <el-empty v-else description="暂无预测结果，请先到预测任务页面运行任务" />

    <PageTable title="预测历史" :rows="rows" :columns="columns" :loading="loading">
      <template #rowActions="{ row }">
        <el-button size="small" type="primary" plain @click="showResult(row)">查看图表</el-button>
      </template>
    </PageTable>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import BaseChart from '@/components/charts/BaseChart.vue'
import PageTable from '@/components/common/PageTable.vue'
import { predictionHistory } from '@/api/prediction'

const rows = ref<any[]>([])
const chart = ref<any[]>([])
const loading = ref(false)
const selectedTaskId = ref<number | null>(null)
const columns = [
  { prop: 'taskId', label: '任务ID' },
  { prop: 'stationId', label: '电站' },
  { prop: 'modelType', label: '预测方法' },
  { prop: 'predictionTime', label: '预测时间' },
  { prop: 'horizon', label: '时长(小时)' }
]

const showResult = (row: any, silent = false) => {
  try {
    const parsed = typeof row.resultData === 'string' ? JSON.parse(row.resultData) : row.resultData
    chart.value = Array.isArray(parsed) ? parsed : []
    selectedTaskId.value = row.taskId
    if (!chart.value.length && !silent) ElMessage.warning('该任务没有可显示的预测数据')
  } catch {
    chart.value = []
    selectedTaskId.value = row.taskId
    if (!silent) ElMessage.error('该任务的预测数据格式不正确')
  }
}

const load = async () => {
  loading.value = true
  try {
    const response: any = await predictionHistory({ page: 1, size: 20 })
    rows.value = response.data.records
    if (rows.value.length) showResult(rows.value[0], true)
    else {
      chart.value = []
      selectedTaskId.value = null
    }
  } finally {
    loading.value = false
  }
}

onMounted(load)

const option = computed(() => ({
  tooltip: { trigger: 'axis' },
  xAxis: { type: 'category', data: chart.value.map(item => item.time) },
  yAxis: { type: 'value', name: '预测功率(kW)' },
  series: [{ name: '预测功率', type: 'line', smooth: true, data: chart.value.map(item => item.power) }]
}))
</script>

<style scoped>
.hint { margin: 4px 0 0; color: #667085; font-size: 13px; }
.chart-title { margin-bottom: 8px; color: #344054; font-weight: 600; }
</style>
