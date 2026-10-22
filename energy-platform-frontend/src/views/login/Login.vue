<template>
  <div class="login">
    <div class="hero"><h1>可再生能源发电功率预测与运维平台</h1><p>风电、光伏、预测、告警与工单闭环管理</p></div>
    <el-card class="login-card"><el-form :model="form" @keyup.enter="submit"><el-form-item><el-input v-model="form.username" placeholder="用户名"/></el-form-item><el-form-item><el-input v-model="form.password" type="password" placeholder="密码" show-password/></el-form-item><el-button type="primary" :loading="loading" style="width:100%" @click="submit">登录</el-button></el-form></el-card>
  </div>
</template>
<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
const form = reactive({ username: 'admin', password: '123456' }); const router = useRouter(); const auth = useAuthStore()
const loading=ref(false);const submit = async () => { loading.value=true;try{await auth.login(form);router.push('/dashboard')}finally{loading.value=false} }
</script>
<style scoped>.login{min-height:100vh;display:grid;grid-template-columns:1.3fr 360px;align-items:center;gap:40px;padding:8vw;background:linear-gradient(120deg,#0f766e,#213a5c)}.hero{color:#fff}.hero h1{font-size:42px;margin:0 0 16px}.login-card{border-radius:8px}.hint{color:#667085;font-size:13px;text-align:center}@media(max-width:760px){.login{grid-template-columns:1fr}.hero h1{font-size:28px}}</style>
