<template>
  <div class="page">
    <div class="toolbar"><h2>工单详情</h2><el-button @click="$router.back()">返回</el-button></div>
    <el-card v-loading="loading">
      <el-descriptions v-if="order" :column="2" border>
        <el-descriptions-item label="工单编号">{{ order.id }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ order.status }}</el-descriptions-item>
        <el-descriptions-item label="标题" :span="2">{{ order.title }}</el-descriptions-item>
        <el-descriptions-item label="说明" :span="2">{{ order.description }}</el-descriptions-item>
        <el-descriptions-item label="电站">{{ order.stationId }}</el-descriptions-item>
        <el-descriptions-item label="设备">{{ order.deviceId }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ order.priority }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ order.assigneeId || '未指派' }}</el-descriptions-item>
      </el-descriptions>
      <div v-if="order" class="actions">
        <template v-if="canManage">
          <el-input-number v-model="assigneeId" :min="1" />
          <el-button type="primary" :disabled="order.status === 'CLOSED'" @click="assign">指派并开始处理</el-button>
        </template>
        <el-button v-if="canResolve" type="success" :disabled="isFinished" @click="complete">标记解决</el-button>
        <el-button v-if="canManage" type="warning" :disabled="order.status !== 'RESOLVED'" @click="close">验收关闭</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { assignWorkOrder, closeWorkOrder, completeWorkOrder, getWorkOrder } from '@/api/workOrder'
import { useAuthStore } from '@/stores/auth'

const auth = useAuthStore()
const id = Number(useRoute().params.id)
const order = ref<any>(null)
const assigneeId = ref(4)
const loading = ref(false)
const canManage = computed(() => auth.permissions.includes('workorder:manage') || auth.permissions.includes('system:manage'))
const canResolve = computed(() => canManage.value || auth.permissions.includes('workorder:resolve'))
const isFinished = computed(() => order.value?.status === 'RESOLVED' || order.value?.status === 'CLOSED')

const load = async () => {
  loading.value = true
  try {
    order.value = (await getWorkOrder(id) as any).data
    assigneeId.value = order.value?.assigneeId || 4
  } finally {
    loading.value = false
  }
}
const assign = async () => {
  await assignWorkOrder(id, assigneeId.value)
  ElMessage.success('工单已指派')
  await load()
}
const complete = async () => {
  await ElMessageBox.confirm('确认该维修工单已经处理完成？', '标记解决', { type: 'warning' })
  await completeWorkOrder(id)
  ElMessage.success('工单已标记为解决')
  await load()
}
const close = async () => {
  await closeWorkOrder(id)
  ElMessage.success('工单已关闭')
  await load()
}

onMounted(load)
</script>

<style scoped>
.actions { display: flex; gap: 12px; align-items: center; margin-top: 20px; }
</style>
