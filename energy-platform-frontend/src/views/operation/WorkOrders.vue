<template>
  <div class="page">
    <div class="toolbar">
      <h2>工单管理</h2>
      <el-button v-if="canManage" type="primary" @click="openCreate">新增工单</el-button>
    </div>
    <PageTable title="工单列表" :rows="rows" :columns="columns" :total="total" :page="page" :loading="loading" @page-change="changePage">
      <template #rowActions="{ row }">
        <el-button size="small" @click="$router.push('/work-orders/' + row.id)">详情</el-button>
        <el-button v-if="canManage" size="small" type="primary" @click="openEdit(row)">编辑</el-button>
        <el-button v-if="canManage" size="small" type="danger" @click="remove(row)">删除</el-button>
      </template>
    </PageTable>

    <el-dialog v-if="canManage" v-model="dialog" :title="form.id ? '编辑工单' : '新增工单'" width="560px">
      <el-form label-width="90px">
        <el-form-item label="标题" required><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="说明"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="电站ID"><el-input-number v-model="form.stationId" :min="1" /></el-form-item>
        <el-form-item label="设备ID"><el-input-number v-model="form.deviceId" :min="1" /></el-form-item>
        <el-form-item label="类型"><el-input v-model="form.type" /></el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="form.priority"><el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" /></el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status"><el-option label="待处理" value="PENDING" /><el-option label="处理中" value="PROCESSING" /><el-option label="已解决" value="RESOLVED" /><el-option label="已关闭" value="CLOSED" /></el-select>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" :loading="saving" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageTable from '@/components/common/PageTable.vue'
import { deleteWorkOrder, listWorkOrders, saveWorkOrder, updateWorkOrder } from '@/api/workOrder'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const canManage = computed(() => auth.permissions.includes('workorder:manage') || auth.permissions.includes('system:manage'))
const rows = ref<any[]>([])
const total = ref(0)
const page = ref(1)
const loading = ref(false)
const dialog = ref(false)
const saving = ref(false)
const form = reactive<any>({})
const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'type', label: '类型' },
  { prop: 'priority', label: '优先级' },
  { prop: 'status', label: '状态' },
  { prop: 'assigneeId', label: '处理人' }
]

const reset = () => Object.assign(form, { id: null, title: '', description: '', stationId: 1, deviceId: null, type: '故障维修', priority: 'MEDIUM', status: 'PENDING' })
const load = async () => {
  loading.value = true
  try {
    const response: any = await listWorkOrders({ page: page.value, size: 10 })
    rows.value = response.data.records
    total.value = response.data.total
  } finally {
    loading.value = false
  }
}
const changePage = (value: number) => { page.value = value; load() }
const openCreate = () => { reset(); dialog.value = true }
const openEdit = (row: any) => { Object.assign(form, row); dialog.value = true }
const submit = async () => {
  if (!form.title) return ElMessage.warning('请填写工单标题')
  saving.value = true
  try {
    form.id ? await updateWorkOrder(form.id, form) : await saveWorkOrder(form)
    dialog.value = false
    ElMessage.success('保存成功')
    await load()
  } finally {
    saving.value = false
  }
}
const remove = async (row: any) => {
  await ElMessageBox.confirm(`确认删除工单“${row.title}”？`, '删除确认', { type: 'warning' })
  await deleteWorkOrder(row.id)
  ElMessage.success('已删除')
  await load()
}

onMounted(load)
</script>
