<template>
  <el-card>
    <template #header><div class="toolbar"><strong>{{ title }}</strong><slot name="actions"/></div></template>
    <el-table v-loading="loading" :data="rows" stripe style="width:100%" empty-text="暂无数据">
      <el-table-column v-for="column in columns" :key="column.prop" :prop="column.prop" :label="column.label" :min-width="column.width || 120" />
      <el-table-column v-if="$slots.rowActions" label="操作" min-width="220" fixed="right">
        <template #default="scope"><slot name="rowActions" v-bind="scope"/></template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total > pageSize" class="pagination" layout="prev,pager,next,total"
      :current-page="page" :page-size="pageSize" :total="total" @current-change="$emit('page-change', $event)" />
  </el-card>
</template>

<script setup lang="ts">
withDefaults(defineProps<{
  title: string
  rows: any[]
  columns: {prop:string, label:string, width?:number}[]
  total?: number
  page?: number
  pageSize?: number
  loading?: boolean
}>(), { total: 0, page: 1, pageSize: 10, loading: false })
defineEmits<{(e:'page-change', page:number):void}>()
</script>

<style scoped>.pagination{margin-top:16px;justify-content:flex-end}</style>
