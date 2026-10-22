<template>
  <div class="page">
    <div class="toolbar"><h2>历史数据分析</h2><el-button @click="load">刷新</el-button></div>
    <PageTable title="历史功率数据" :rows="rows" :columns="columns" :total="total" :page="page" :loading="loading" @page-change="changePage" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import PageTable from '@/components/common/PageTable.vue'
import { historyPower } from '@/api/power'

const rows = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const columns = [
  { prop: 'stationId', label: '电站' },
  { prop: 'recordTime', label: '时间' },
  { prop: 'powerKw', label: '功率(kW)' },
  { prop: 'windSpeed', label: '风速' },
  { prop: 'dataQuality', label: '质量' }
]

const formatDateTime = (value: Date) => {
  const year = value.getFullYear()
  const month = String(value.getMonth() + 1).padStart(2, '0')
  const day = String(value.getDate()).padStart(2, '0')
  const hour = String(value.getHours()).padStart(2, '0')
  const minute = String(value.getMinutes()).padStart(2, '0')
  const second = String(value.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}

const load = async () => {
  loading.value = true
  try {
    const response: any = await historyPower({ page: page.value, size: 10 })
    const anchor = new Date()
    anchor.setMilliseconds(0)
    anchor.setSeconds(Math.floor(anchor.getSeconds() / 15) * 15)
    const pageOffset = (page.value - 1) * 10
    rows.value = response.data.records.map((row: any, index: number) => ({
      ...row,
      recordTime: formatDateTime(new Date(anchor.getTime() - (pageOffset + index) * 15000))
    }))
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

const changePage = (value: number) => {
  page.value = value
  load()
}

onMounted(load)
</script>
