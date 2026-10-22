<template>
  <div class="page">
    <div class="toolbar"><h2>电站管理</h2><div class="filters"><el-input v-model="keyword" clearable placeholder="名称、编码或地点" @keyup.enter="search"/><el-button @click="search">查询</el-button></div></div>
    <PageTable title="电站列表" :rows="rows" :columns="columns" :total="total" :page="page" :loading="loading" @page-change="changePage">
      <template #actions><el-button type="primary" @click="openCreate">新增电站</el-button></template>
      <template #rowActions="{row}"><el-button size="small" @click="$router.push('/stations/'+row.id)">详情</el-button><el-button size="small" type="primary" @click="openEdit(row)">编辑</el-button><el-button size="small" type="danger" @click="remove(row)">删除</el-button></template>
    </PageTable>
    <el-dialog v-model="dialog" :title="form.id ? '编辑电站' : '新增电站'" width="560px">
      <el-form label-width="90px"><el-form-item label="名称" required><el-input v-model="form.name"/></el-form-item><el-form-item label="编码" required><el-input v-model="form.code"/></el-form-item><el-form-item label="类型" required><el-select v-model="form.type"><el-option label="风电" value="WIND"/><el-option label="光伏" value="SOLAR"/></el-select></el-form-item><el-form-item label="容量 kW"><el-input-number v-model="form.capacity" :min="0"/></el-form-item><el-form-item label="地点"><el-input v-model="form.location"/></el-form-item><el-form-item label="状态"><el-select v-model="form.status"><el-option label="运行中" value="RUNNING"/><el-option label="维护中" value="MAINTENANCE"/><el-option label="停用" value="STOPPED"/></el-select></el-form-item></el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" :loading="saving" @click="submit">保存</el-button></template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import PageTable from '@/components/common/PageTable.vue'
import { deleteStation, listStations, saveStation, updateStation } from '@/api/station'
const rows=ref<any[]>([]), total=ref(0), keyword=ref(''), page=ref(1), loading=ref(false), dialog=ref(false), saving=ref(false)
const form=reactive<any>({}); const columns=[{prop:'name',label:'电站名称'},{prop:'code',label:'编码'},{prop:'type',label:'类型'},{prop:'capacity',label:'容量(kW)'},{prop:'status',label:'状态'}]
const reset=()=>Object.assign(form,{id:null,name:'',code:'',type:'WIND',capacity:0,location:'',status:'RUNNING'})
const load=async()=>{loading.value=true;try{const r:any=await listStations({page:page.value,size:10,keyword:keyword.value});rows.value=r.data.records;total.value=r.data.total}finally{loading.value=false}}
const search=()=>{page.value=1;load()}; const changePage=(value:number)=>{page.value=value;load()}
const openCreate=()=>{reset();dialog.value=true}; const openEdit=(row:any)=>{Object.assign(form,row);dialog.value=true}
const submit=async()=>{if(!form.name||!form.code)return ElMessage.warning('请填写名称和编码');saving.value=true;try{form.id?await updateStation(form.id,form):await saveStation(form);ElMessage.success('保存成功');dialog.value=false;await load()}finally{saving.value=false}}
const remove=async(row:any)=>{await ElMessageBox.confirm(`确认删除电站“${row.name}”？`,'删除确认',{type:'warning'});await deleteStation(row.id);ElMessage.success('已删除');load()}
onMounted(load)
</script>
<style scoped>.filters{display:flex;gap:10px;width:420px}.filters .el-input{flex:1}</style>
