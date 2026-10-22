<template>
  <div class="page">
    <div class="toolbar">
      <h2>预测任务</h2>
      <el-button type="primary" @click="openCreate">新建预测任务</el-button>
    </div>
    <PageTable title="预测任务列表" :rows="rows" :columns="columns" :total="total" :page="page" :loading="loading" @page-change="changePage">
      <template #rowActions="{ row }">
        <el-button
          size="small"
          type="primary"
          :loading="runningId === row.id"
          :disabled="!canRun(row)"
          @click="run(row)"
        >运行</el-button>
        <el-button size="small" type="danger" @click="remove(row)">删除</el-button>
      </template>
    </PageTable>

    <el-dialog v-model="dialog" title="新建预测任务" width="480px">
      <el-form label-width="100px">
        <el-form-item label="电站" required>
          <el-select v-model="form.stationId" placeholder="请选择电站" style="width: 100%">
            <el-option v-for="station in stations" :key="station.id" :label="station.name" :value="station.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预测时长">
          <el-input-number v-model="form.horizon" :min="1" :max="24" /><span class="unit">小时</span>
        </el-form-item>
        <el-form-item label="置信水平">
          <el-input-number v-model="form.confidenceLevel" :min="50" :max="99" /><span class="unit">%</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog=false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="create">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageTable from '@/components/common/PageTable.vue'
import { createPredictionTask, deletePredictionTask, predictionTasks, runPredictionTask } from '@/api/prediction'
import { listStations } from '@/api/station'

const rows = ref<any[]>([])
const stations = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const dialog = ref(false)
const saving = ref(false)
const runningId = ref<number | null>(null)
const form = reactive({ stationId: undefined as number | undefined, modelType: 'Attention-Heuristic', horizon: 2, confidenceLevel: 90 })
const columns = [
  { prop: 'stationId', label: '电站ID' },
  { prop: 'modelType', label: '预测方法' },
  { prop: 'horizon', label: '时长(小时)' },
  { prop: 'confidenceLevel', label: '置信水平' },
  { prop: 'status', label: '状态' }
]

const load = async () => {
  loading.value = true
  try {
    const response: any = await predictionTasks({ page: page.value, size: 10 })
    rows.value = response.data.records
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}

const loadStations = async () => {
  const response: any = await listStations({ page: 1, size: 100 })
  stations.value = response.data.records
}

const openCreate = () => {
  form.stationId = stations.value[0]?.id
  form.horizon = 2
  form.confidenceLevel = 90
  dialog.value = true
}

const changePage = (value: number) => {
  page.value = value
  load()
}

const create = async () => {
  if (!form.stationId) {
    ElMessage.warning('请选择电站')
    return
  }
  saving.value = true
  try {
    await createPredictionTask(form)
    dialog.value = false
    page.value = 1
    ElMessage.success('任务已创建，可在列表中运行')
    await load()
  } finally {
    saving.value = false
  }
}

const canRun = (row: any) => row.status === 'PENDING' || row.status === 'FAILED'

const run = async (row: any) => {
  runningId.value = row.id
  try {
    await runPredictionTask(row.id)
    ElMessage.success('预测完成，可到预测结果页面查看')
    await load()
  } finally {
    runningId.value = null
  }
}

const remove = async (row: any) => {
  await ElMessageBox.confirm('确认删除该预测任务？', '删除确认', { type: 'warning' })
  await deletePredictionTask(row.id)
  ElMessage.success('已删除')
  await load()
}

onMounted(async () => {
  await Promise.all([load(), loadStations()])
})
</script>

<style scoped>
.unit { margin-left: 8px; color: #667085; }
</style>
