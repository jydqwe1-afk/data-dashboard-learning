import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '@/store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/components/Layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页', icon: 'home' }
      },
      {
        path: 'archive',
        name: 'Archive',
        redirect: '/archive/management',
        meta: { title: '档案管理', icon: 'folder' },
        children: [
          {
            path: 'management',
            name: 'ArchiveManagement',
            component: () => import('@/views/archive/Management.vue'),
            meta: { title: '档案列表' }
          },
          {
            path: 'upload',
            name: 'ArchiveUpload',
            component: () => import('@/views/archive/Upload.vue'),
            meta: { title: '档案上传' }
          }
        ]
      },
      {
        path: 'search',
        name: 'Search',
        component: () => import('@/views/search/Search.vue'),
        meta: { title: '档案查询', icon: 'search' }
      },
      {
        path: 'reader/:id?',
        name: 'Reader',
        component: () => import('@/views/reader/Reader.vue'),
        meta: { title: '档案阅读', icon: 'read' }
      },
      {
        path: 'application',
        name: 'Application',
        component: () => import('@/views/application/Application.vue'),
        meta: { title: '查档管理', icon: 'file-protect' }
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { title: '系统管理', icon: 'setting', roles: ['admin'] },
        children: [
          {
            path: 'user',
            name: 'UserManage',
            component: () => import('@/views/system/UserManage.vue'),
            meta: { title: '用户管理' }
          },
          {
            path: 'dept',
            name: 'DeptManage',
            component: () => import('@/views/system/DeptManage.vue'),
            meta: { title: '部门管理' }
          },
          {
            path: 'log',
            name: 'LogManage',
            component: () => import('@/views/system/LogManage.vue'),
            meta: { title: '操作日志' }
          }
        ]
      }
    ]
  },
  { path: '*', redirect: '/dashboard' }
]

const router = new VueRouter({ mode: 'hash', routes })

router.beforeEach((to, from, next) => {
  const token = store.state.user.token
  if (to.meta.requiresAuth === false) {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
