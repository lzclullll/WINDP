import { createRouter, createWebHashHistory } from 'vue-router'
import { isAuthenticated } from '@/utils/auth'
import Layout from '@/components/layout/AppLayout.vue'

export const menuRoutes = [
  { path: '/dashboard', name: '综合看板', component: () => import('@/views/dashboard/Dashboard.vue'), permission: 'dashboard:view' },
  { path: '/stations', name: '电站管理', component: () => import('@/views/station/StationList.vue'), permission: 'station:manage' },
  { path: '/stations/:id', name: '电站详情', component: () => import('@/views/station/StationDetail.vue'), hidden: true },
  { path: '/devices', name: '设备管理', component: () => import('@/views/device/DeviceList.vue'), permission: 'device:manage' },
  { path: '/power/realtime', name: '实时功率', component: () => import('@/views/power/RealtimePower.vue'), permission: 'power:view' },
  { path: '/power/history', name: '历史分析', component: () => import('@/views/power/HistoryAnalysis.vue'), permission: 'power:view' },
  { path: '/prediction/tasks', name: '预测任务', component: () => import('@/views/prediction/PredictionTasks.vue'), permission: 'prediction:manage' },
  { path: '/prediction/results', name: '预测结果', component: () => import('@/views/prediction/PredictionResults.vue'), permission: 'prediction:manage' },
  { path: '/assistant', name: '运维知识助手', component: () => import('@/views/assistant/KnowledgeAssistant.vue'), permission: 'assistant:use' },
  { path: '/work-orders', name: '工单管理', component: () => import('@/views/operation/WorkOrders.vue'), permission: ['workorder:view', 'workorder:resolve', 'workorder:manage'] },
  { path: '/work-orders/:id', name: '工单详情', component: () => import('@/views/operation/WorkOrderDetail.vue'), permission: ['workorder:view', 'workorder:resolve', 'workorder:manage'], hidden: true },
  { path: '/alarms', name: '告警列表', component: () => import('@/views/alarm/AlarmList.vue'), permission: 'alarm:manage' },
  { path: '/alarm-rules', name: '告警规则', component: () => import('@/views/alarm/AlarmRules.vue'), permission: 'alarm:manage' },
  { path: '/reports', name: '报表管理', component: () => import('@/views/report/ReportList.vue'), permission: 'report:view' },
  { path: '/system/users', name: '用户管理', component: () => import('@/views/system/UserList.vue'), permission: 'system:manage' },
  { path: '/system/roles', name: '角色权限', component: () => import('@/views/system/RolePermission.vue'), permission: 'system:manage' },
  { path: '/system/logs', name: '系统日志', component: () => import('@/views/system/SystemLogs.vue'), permission: 'system:manage' },
  { path: '/system/login-logs', name: '登录日志', component: () => import('@/views/system/LoginLogs.vue'), permission: 'system:manage' }
]
const router = createRouter({ history: createWebHashHistory(), routes: [
  { path: '/login', component: () => import('@/views/login/Login.vue') },
  { path: '/', component: Layout, redirect: '/dashboard', children: menuRoutes },
  { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
]})
router.beforeEach((to) => {
  if (to.path !== '/login' && !isAuthenticated()) return '/login'
  if (to.path === '/login' && isAuthenticated()) return '/dashboard'
})
export default router
