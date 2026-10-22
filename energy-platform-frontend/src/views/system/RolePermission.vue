<template>
  <div class="page">
    <div class="toolbar">
      <h2>角色权限</h2>
      <el-input v-model="keyword" clearable placeholder="搜索角色编码或名称" style="max-width:280px" @keyup.enter="load(1)" @clear="load(1)" />
    </div>
    <PageTable title="角色列表" :rows="rows" :columns="columns" :total="total" :page="page" :page-size="size" :loading="loading" @page-change="load" />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import PageTable from '@/components/common/PageTable.vue'
import { listRoles } from '@/api/role'

const rows = ref<any[]>([])
const total = ref(0)
const keyword = ref('')
const page = ref(1)
const size = 10
const loading = ref(false)
const columns = [
  { prop: 'roleCode', label: '角色编码' },
  { prop: 'roleName', label: '角色名称' },
  { prop: 'description', label: '说明' }
]
const load = async (target = page.value) => {
  page.value = target
  loading.value = true
  try {
    const response: any = await listRoles({ page: page.value, size, keyword: keyword.value })
    rows.value = response.data.records
    total.value = response.data.total
  } finally { loading.value = false }
}
onMounted(() => load())
</script>
