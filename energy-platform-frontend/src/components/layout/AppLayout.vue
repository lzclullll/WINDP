<template>
  <div :class="['shell', app.dark && 'dark']">
    <aside class="sidebar">
      <div class="brand">能源预测运维平台</div>
      <el-menu router :default-active="activePath" :default-openeds="defaultOpenGroups"
        background-color="transparent" text-color="#cbd6e2" active-text-color="#ffffff">
        <template v-for="group in visibleMenuGroups" :key="group.key">
          <el-menu-item v-if="group.direct" :index="group.items[0].path">{{ group.title }}</el-menu-item>
          <el-sub-menu v-else :index="group.key">
            <template #title>{{ group.title }}</template>
            <el-menu-item v-for="item in group.items" :key="item.path" :index="item.path">{{ item.name }}</el-menu-item>
          </el-sub-menu>
        </template>
      </el-menu>
    </aside>
    <main class="main">
      <header class="topbar">
        <el-breadcrumb><el-breadcrumb-item>{{ route.name }}</el-breadcrumb-item></el-breadcrumb>
        <div class="top-actions"><el-switch :model-value="app.dark" @change="app.toggleTheme" active-text="暗色" inactive-text="亮色"/><el-dropdown><span>{{ auth.realName || auth.username }}</span><template #dropdown><el-dropdown-menu><el-dropdown-item @click="signOut">退出登录</el-dropdown-item></el-dropdown-menu></template></el-dropdown></div>
      </header>
      <section class="content"><router-view /></section>
    </main>
  </div>
</template>
<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { menuRoutes } from '@/router'
import { useAuthStore } from '@/stores/auth'
import { useAppStore } from '@/stores/app'
const route = useRoute(); const router = useRouter(); const auth = useAuthStore(); const app = useAppStore()
const routeMap = Object.fromEntries(menuRoutes.filter((item:any) => !item.hidden).map((item:any) => [item.path, item]))
const menuGroups = [
  { key: 'dashboard', title: '综合看板', direct: true, paths: ['/dashboard'] },
  { key: 'resources', title: '场站资源', paths: ['/stations', '/devices'] },
  { key: 'power', title: '功率监测', paths: ['/power/realtime', '/power/history'] },
  { key: 'prediction', title: '预测管理', paths: ['/prediction/tasks', '/prediction/results'] },
  { key: 'operation', title: '运维管理', paths: ['/alarms', '/alarm-rules', '/work-orders'] },
  { key: 'data', title: '数据服务', paths: ['/assistant', '/reports'] },
  { key: 'system', title: '系统管理', paths: ['/system/users', '/system/roles', '/system/logs', '/system/login-logs'] }
]
const canView = (item:any) => {
  if (!item.permission || auth.permissions.includes('system:manage')) return true
  const required = Array.isArray(item.permission) ? item.permission : [item.permission]
  return required.some((permission:string) => auth.permissions.includes(permission))
}
const visibleMenuGroups = computed(() => menuGroups.map(group => ({
  ...group,
  items: group.paths.map(path => routeMap[path]).filter(item => item && canView(item))
})).filter(group => group.items.length > 0))
const activePath = computed(() => route.path)
const defaultOpenGroups = computed(() => visibleMenuGroups.value
  .filter(group => !group.direct && group.items.some((item:any) => route.path === item.path || route.path.startsWith(item.path + '/')))
  .map(group => group.key))
const signOut = async () => { await auth.logout(); router.push('/login') }
</script>
<style scoped>
.shell{min-height:100vh;background:#eef3f8}.sidebar{position:fixed;left:0;top:0;width:224px;height:100vh;background:#132238;padding:16px 10px;overflow-y:auto}.brand{color:#fff;font-weight:700;font-size:18px;padding:10px 14px 18px}.sidebar :deep(.el-menu){border-right:0}.sidebar :deep(.el-menu-item),.sidebar :deep(.el-sub-menu__title){height:44px;line-height:44px;border-radius:6px;margin:2px 0}.sidebar :deep(.el-menu-item:hover),.sidebar :deep(.el-sub-menu__title:hover){background:#1d334f}.sidebar :deep(.el-menu-item.is-active){background:#0f766e}.sidebar :deep(.el-sub-menu .el-menu-item){padding-left:42px!important}.main{margin-left:224px;min-height:100vh}.topbar{height:58px;background:#fff;border-bottom:1px solid #dde6f0;display:flex;align-items:center;justify-content:space-between;padding:0 20px}.top-actions{display:flex;align-items:center;gap:16px}.content{padding:18px}.dark .topbar{background:#172233;border-color:#26374c;color:#e7eef7}
</style>
