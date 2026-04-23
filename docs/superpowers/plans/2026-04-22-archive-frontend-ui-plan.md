# 电子档案管理系统前端界面实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 开发电子档案管理系统前端Vue2界面，包含登录页面、主布局框架、各功能模块静态页面，采用Notion设计风格 + Ant Design Vue组件库

**Architecture:** 基于Vue2 + Ant Design Vue的前后端分离单页应用，使用Vue Router管理路由，Vuex管理状态，Axios调用后端RESTful API，AntV实现数据可视化

**Tech Stack:** Vue2, Ant Design Vue 1.x, Vue Router 3, Vuex 3, Axios, AntV/G2, Less

---

## 文件结构

```
archive-management-frontend/
├── public/
│   └── index.html
├── src/
│   ├── api/                        # API接口定义
│   │   ├── http.js                 # Axios统一配置
│   │   ├── user.js                 # 用户相关API
│   │   ├── archive.js              # 档案相关API
│   │   └── application.js          # 查档申请API
│   ├── assets/
│   │   └── styles/
│   │       └── global.less         # 全局样式（Notion主题定制）
│   ├── components/
│   │   └── Layout/
│   │       ├── MainLayout.vue      # 主布局
│   │       └── PageHeader.vue      # 页面头部
│   ├── router/
│   │   └── index.js                # 路由配置
│   ├── store/
│   │   ├── index.js                # Vuex入口
│   │   └── modules/
│   │       └── user.js             # 用户状态模块
│   ├── views/
│   │   ├── Login.vue               # 登录页面
│   │   ├── Dashboard.vue           # 首页仪表盘
│   │   ├── archive/
│   │   │   ├── Management.vue      # 档案管理
│   │   │   └── Upload.vue          # 档案上传
│   │   ├── search/
│   │   │   └── Search.vue          # 档案查询
│   │   ├── reader/
│   │   │   └── Reader.vue          # 档案阅读
│   │   ├── application/
│   │   │   └── Application.vue     # 查档管理
│   │   └── system/
│   │       ├── UserManage.vue      # 用户管理
│   │       ├── DeptManage.vue      # 部门管理
│   │       └── LogManage.vue       # 日志管理
│   ├── App.vue                     # 根组件
│   └── main.js                     # 入口文件
├── vue.config.js
└── package.json
```

---

## Task 1: 项目初始化

**Files:**
- Create: `package.json`
- Create: `vue.config.js`
- Create: `public/index.html`
- Create: `src/main.js`
- Create: `src/App.vue`

- [ ] **Step 1: 创建package.json**

```json
{
  "name": "archive-management-frontend",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "serve": "vue-cli-service serve",
    "build": "vue-cli-service build"
  },
  "dependencies": {
    "vue": "^2.7.16",
    "vue-router": "^3.6.5",
    "vuex": "^3.6.2",
    "ant-design-vue": "^1.7.8",
    "axios": "^1.7.9",
    "@antv/g2": "^3.5.19"
  },
  "devDependencies": {
    "@vue/cli-service": "^5.0.8",
    "vue-template-compiler": "^2.7.16",
    "less": "^3.13.1",
    "less-loader": "^5.0.0"
  }
}
```

- [ ] **Step 2: 创建vue.config.js**

```javascript
module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true
      }
    }
  },
  lintOnSave: false,
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          'primary-color': '#0075de',
          'link-color': '#0075de',
          'border-radius-base': '4px',
          'font-family': "Inter, -apple-system, system-ui, 'Segoe UI', Helvetica, Arial, sans-serif"
        },
        javascriptEnabled: true
      }
    }
  }
}
```

- [ ] **Step 3: 创建public/index.html**

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width,initial-scale=1.0">
  <title>电子档案管理系统</title>
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
  <div id="app"></div>
</body>
</html>
```

- [ ] **Step 4: 创建src/main.js**

```javascript
import Vue from 'vue'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.less'
import App from './App.vue'
import router from './router'
import store from './store'
import './assets/styles/global.less'

Vue.use(Antd)
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
```

- [ ] **Step 5: 创建src/App.vue**

```vue
<template>
  <a-config-provider :locale="locale">
    <div id="app">
      <router-view />
    </div>
  </a-config-provider>
</template>

<script>
import zhCN from 'ant-design-vue/lib/locale-provider/zh_CN'
export default {
  name: 'App',
  data() {
    return { locale: zhCN }
  }
}
</script>

<style lang="less">
#app {
  font-family: 'Inter', -apple-system, system-ui, 'Segoe UI', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  height: 100%;
}
</style>
```

- [ ] **Step 6: 安装依赖并验证**

```bash
cd archive-management-frontend
npm install
npm run serve
```

预期：开发服务器启动在 http://localhost:8080，页面空白无报错

- [ ] **Step 7: 提交**

```bash
git add .
git commit -m "feat: 初始化前端项目 Vue2+AntDesignVue"
```

---

## Task 2: 全局样式（Notion主题定制）

**Files:**
- Create: `src/assets/styles/global.less`

- [ ] **Step 1: 创建全局Less样式文件**

```less
:root {
  --color-primary: #0075de;
  --color-primary-active: #005bab;
  --color-bg: #ffffff;
  --color-bg-warm: #f6f5f4;
  --color-text-primary: rgba(0, 0, 0, 0.95);
  --color-text-secondary: #615d59;
  --color-text-muted: #a39e98;
  --color-border: rgba(0, 0, 0, 0.1);
}

* { margin: 0; padding: 0; box-sizing: border-box; }
html, body { height: 100%; }

// Notion风格页面容器
.page-container {
  padding: 24px;
  background: var(--color-bg);
  min-height: calc(100vh - 64px);
}

.page-header {
  margin-bottom: 24px;
  h2 {
    font-size: 26px;
    font-weight: 700;
    letter-spacing: -0.625px;
    color: var(--color-text-primary);
    margin-bottom: 4px;
  }
  p { color: var(--color-text-secondary); }
}

// 卡片容器
.card-container {
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  padding: 24px;
}

// 统计卡片网格
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 24px;
  background: var(--color-bg);
  border: 1px solid var(--color-border);
  border-radius: 12px;
  .stat-icon {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 48px;
    height: 48px;
    border-radius: 8px;
    i { font-size: 24px; }
  }
  .stat-info {
    .stat-value {
      font-size: 28px;
      font-weight: 700;
      line-height: 1;
    }
    .stat-label {
      font-size: 14px;
      color: var(--color-text-secondary);
      margin-top: 4px;
    }
  }
}

// 工具栏
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

// 分页容器
.pagination-wrapper {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid var(--color-border);
}

// 危险按钮
.danger-text { color: #ff4d4f !important; }
```

- [ ] **Step 2: 验证**

运行 `npm run serve`，检查控制台无样式错误。

- [ ] **Step 3: 提交**

```bash
git add src/assets/styles/global.less
git commit -m "feat: 添加Notion风格全局样式"
```

---

## Task 3: Axios封装与API模块

**Files:**
- Create: `src/api/http.js`
- Create: `src/api/user.js`
- Create: `src/api/archive.js`
- Create: `src/api/application.js`

- [ ] **Step 1: 创建src/api/http.js（Axios统一配置）**

```javascript
import axios from 'axios'
import store from '@/store'
import router from '@/router'
import { message } from 'ant-design-vue'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000
})

http.interceptors.request.use(config => {
  const token = store.state.user.token
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

http.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      message.error(res.message || '请求失败')
      if (res.code === 401) {
        store.dispatch('user/logout')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message))
    }
    return res.data
  },
  error => {
    message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default http
```

- [ ] **Step 2: 创建src/api/user.js**

```javascript
import http from './http'

export function login(data) {
  return http.post('/auth/login', data)
}

export function getUserInfo() {
  return http.get('/auth/info')
}

export function getUserList(params) {
  return http.get('/system/users', { params })
}

export function createUser(data) {
  return http.post('/system/users', data)
}

export function updateUser(id, data) {
  return http.put(`/system/users/${id}`, data)
}

export function deleteUser(id) {
  return http.delete(`/system/users/${id}`)
}
```

- [ ] **Step 3: 创建src/api/archive.js**

```javascript
import http from './http'

export function getFolderTree() {
  return http.get('/archive/folders')
}

export function createFolder(data) {
  return http.post('/archive/folders', data)
}

export function getArchiveList(params) {
  return http.get('/archives', { params })
}

export function getArchiveDetail(id) {
  return http.get(`/archives/${id}`)
}

export function createArchive(data) {
  return http.post('/archives', data)
}

export function updateArchive(id, data) {
  return http.put(`/archives/${id}`, data)
}

export function deleteArchive(id) {
  return http.delete(`/archives/${id}`)
}

export function searchArchives(params) {
  return http.get('/archives/search', { params })
}

export function uploadFile(formData) {
  return http.post('/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function downloadFile(fileId) {
  return http.get(`/files/${fileId}/download`, { responseType: 'blob' })
}

export function getCategories() {
  return http.get('/archive/categories')
}

export function getSecurityLevels() {
  return http.get('/archive/security-levels')
}
```

- [ ] **Step 4: 创建src/api/application.js**

```javascript
import http from './http'

export function getMyApplications(params) {
  return http.get('/applications/mine', { params })
}

export function createApplication(data) {
  return http.post('/applications', data)
}

export function getPendingReviews(params) {
  return http.get('/applications/reviews', { params })
}

export function reviewApplication(id, data) {
  return http.put(`/applications/${id}/review`, data)
}
```

- [ ] **Step 5: 提交**

```bash
git add src/api/
git commit -m "feat: 封装Axios拦截器和API接口模块"
```

---

## Task 4: 路由配置与Vuex状态管理

**Files:**
- Create: `src/router/index.js`
- Create: `src/store/index.js`
- Create: `src/store/modules/user.js`

- [ ] **Step 1: 创建src/router/index.js**

```javascript
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
```

- [ ] **Step 2: 创建src/store/index.js**

```javascript
import Vue from 'vue'
import Vuex from 'vuex'
import user from './modules/user'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: { user }
})
```

- [ ] **Step 3: 创建src/store/modules/user.js**

```javascript
const state = {
  token: localStorage.getItem('token') || '',
  userInfo: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  role: localStorage.getItem('role') || ''
}

const getters = {
  isLoggedIn: state => !!state.token,
  isAdmin: state => state.role === 'admin',
  isDeptAdmin: state => state.role === 'dept_admin',
  currentUser: state => state.userInfo
}

const mutations = {
  SET_TOKEN(state, token) {
    state.token = token
    localStorage.setItem('token', token)
  },
  SET_USER_INFO(state, userInfo) {
    state.userInfo = userInfo
    localStorage.setItem('userInfo', JSON.stringify(userInfo))
  },
  SET_ROLE(state, role) {
    state.role = role
    localStorage.setItem('role', role)
  },
  LOGOUT(state) {
    state.token = ''
    state.userInfo = {}
    state.role = ''
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('role')
  }
}

const actions = {
  login({ commit }, { token, userInfo, role }) {
    commit('SET_TOKEN', token)
    commit('SET_USER_INFO', userInfo)
    commit('SET_ROLE', role)
  },
  logout({ commit }) {
    commit('LOGOUT')
  }
}

export default { namespaced: true, state, getters, mutations, actions }
```

- [ ] **Step 4: 提交**

```bash
git add src/router/ src/store/
git commit -m "feat: 配置路由和Vuex状态管理"
```

---

## Task 5: 登录页面

**Files:**
- Create: `src/views/Login.vue`

- [ ] **Step 1: 创建登录页面**

```vue
<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>电子档案管理系统</h1>
        <p>长乐区水务投资集团有限公司</p>
      </div>
      <a-form-model
        ref="loginForm"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <a-form-model-item prop="username">
          <a-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
          >
            <a-icon slot="prefix" type="user" />
          </a-input>
        </a-form-model-item>
        <a-form-model-item prop="password">
          <a-input-password
            v-model="loginForm.password"
            placeholder="请输入密码"
            size="large"
            @pressEnter="handleLogin"
          >
            <a-icon slot="prefix" type="lock" />
          </a-input-password>
        </a-form-model-item>
        <a-form-model-item>
          <a-button
            type="primary"
            :loading="loading"
            size="large"
            block
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      loginForm: { username: '', password: '' },
      loginRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码长度不少于6位', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          setTimeout(() => {
            this.$store.dispatch('user/login', {
              token: 'mock-token-' + Date.now(),
              userInfo: { id: 1, username: this.loginForm.username, real_name: '管理员' },
              role: 'admin'
            })
            this.$message.success('登录成功')
            this.$router.push('/dashboard')
            this.loading = false
          }, 1000)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: var(--color-bg-warm);
}
.login-card {
  width: 400px;
  padding: 48px 40px;
  background: #fff;
  border: 1px solid var(--color-border);
  border-radius: 16px;
  box-shadow: 0 23px 52px rgba(0, 0, 0, 0.05);
}
.login-header {
  text-align: center;
  margin-bottom: 40px;
  h1 {
    font-size: 26px;
    font-weight: 700;
    letter-spacing: -0.625px;
    color: var(--color-text-primary);
    margin-bottom: 8px;
  }
  p { font-size: 14px; color: var(--color-text-secondary); }
}
.login-form .ant-form-item { margin-bottom: 24px; }
</style>
```

- [ ] **Step 2: 验证**

访问 http://localhost:8080，应显示登录页，表单验证和模拟登录正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/Login.vue
git commit -m "feat: 实现Notion风格登录页面"
```

---

## Task 6: 主布局组件

**Files:**
- Create: `src/components/Layout/MainLayout.vue`
- Create: `src/components/Layout/PageHeader.vue`

- [ ] **Step 1: 创建PageHeader.vue**

```vue
<template>
  <div class="page-header-bar">
    <div class="header-left">
      <a-icon
        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
        class="collapse-btn"
        @click="$emit('toggle-collapse')"
      />
      <span class="breadcrumb-text">{{ currentTitle }}</span>
    </div>
    <div class="header-right">
      <a-dropdown>
        <span class="user-dropdown">
          <a-icon type="user" style="color: #0075de;" />
          <span>{{ userInfo.real_name || userInfo.username }}</span>
          <a-icon type="down" />
        </span>
        <a-menu slot="overlay" @click="handleMenuClick">
          <a-menu-item key="profile"><a-icon type="user" />个人信息</a-menu-item>
          <a-menu-divider />
          <a-menu-item key="logout"><a-icon type="logout" />退出登录</a-menu-item>
        </a-menu>
      </a-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'PageHeader',
  props: { collapsed: Boolean },
  computed: {
    ...mapGetters('user', ['currentUser']),
    userInfo() { return this.currentUser },
    currentTitle() { return this.$route.meta?.title || '首页' }
  },
  methods: {
    handleMenuClick({ key }) {
      if (key === 'logout') {
        this.$confirm({
          title: '确定要退出登录吗？',
          onOk: () => {
            this.$store.dispatch('user/logout')
            this.$router.push('/login')
            this.$message.success('已退出登录')
          }
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.page-header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 56px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid var(--color-border);
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
  .collapse-btn { font-size: 18px; cursor: pointer; color: var(--color-text-secondary); }
  .breadcrumb-text { font-size: 16px; font-weight: 600; }
}
.header-right .user-dropdown {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  &:hover { background: var(--color-bg-warm); }
}
</style>
```

- [ ] **Step 2: 创建MainLayout.vue**

```vue
<template>
  <a-layout class="main-layout">
    <a-layout-sider
      :collapsed="collapsed"
      :trigger="null"
      collapsible
      width="220"
      class="sidebar"
    >
      <div class="logo" @click="$router.push('/dashboard')">
        <a-icon type="folder-open" style="font-size: 22px; color: #0075de;" />
        <span v-show="!collapsed">档案管理</span>
      </div>
      <a-menu
        theme="light"
        mode="inline"
        :selected-keys="[$route.path]"
        :open-keys="openKeys"
        @openChange="onOpenChange"
      >
        <a-menu-item key="/dashboard" @click="$router.push('/dashboard')">
          <a-icon type="home" /><span>首页</span>
        </a-menu-item>
        <a-sub-menu key="/archive">
          <span slot="title"><a-icon type="folder" /><span>档案管理</span></span>
          <a-menu-item key="/archive/management" @click="$router.push('/archive/management')">档案列表</a-menu-item>
          <a-menu-item key="/archive/upload" @click="$router.push('/archive/upload')">档案上传</a-menu-item>
        </a-sub-menu>
        <a-menu-item key="/search" @click="$router.push('/search')">
          <a-icon type="search" /><span>档案查询</span>
        </a-menu-item>
        <a-menu-item key="/reader" @click="$router.push('/reader')">
          <a-icon type="read" /><span>档案阅读</span>
        </a-menu-item>
        <a-menu-item key="/application" @click="$router.push('/application')">
          <a-icon type="file-protect" /><span>查档管理</span>
        </a-menu-item>
        <a-sub-menu v-if="isAdmin" key="/system">
          <span slot="title"><a-icon type="setting" /><span>系统管理</span></span>
          <a-menu-item key="/system/user" @click="$router.push('/system/user')">用户管理</a-menu-item>
          <a-menu-item key="/system/dept" @click="$router.push('/system/dept')">部门管理</a-menu-item>
          <a-menu-item key="/system/log" @click="$router.push('/system/log')">操作日志</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="layout-header">
        <PageHeader :collapsed="collapsed" @toggle-collapse="collapsed = !collapsed" />
      </a-layout-header>
      <a-layout-content class="layout-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
import { mapGetters } from 'vuex'
import PageHeader from './PageHeader.vue'
export default {
  name: 'MainLayout',
  components: { PageHeader },
  data() {
    return { collapsed: false, openKeys: [] }
  },
  computed: {
    ...mapGetters('user', ['isAdmin'])
  },
  created() {
    const parentPath = '/' + this.$route.path.split('/')[1]
    this.openKeys = [parentPath]
  },
  methods: {
    onOpenChange(openKeys) {
      this.openKeys = openKeys
    }
  }
}
</script>

<style lang="less" scoped>
.main-layout { height: 100vh; }
.sidebar {
  background: #fff;
  border-right: 1px solid var(--color-border);
  overflow: auto;
}
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  height: 56px;
  cursor: pointer;
  border-bottom: 1px solid var(--color-border);
  span {
    font-size: 16px;
    font-weight: 700;
    white-space: nowrap;
  }
}
.layout-header { padding: 0; height: auto; background: #fff; }
.layout-content { margin: 0; background: var(--color-bg-warm); }
</style>
```

- [ ] **Step 3: 验证**

登录后看到：左侧折叠菜单、顶部用户信息，菜单点击路由跳转正常。

- [ ] **Step 4: 提交**

```bash
git add src/components/Layout/
git commit -m "feat: 实现主布局框架，侧边栏+顶部栏"
```

---

## Task 7: 首页仪表盘（含AntV图表）

**Files:**
- Create: `src/views/Dashboard.vue`

- [ ] **Step 1: 创建首页仪表盘**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>欢迎回来，{{ userInfo.real_name || userInfo.username }}</h2>
      <p>电子档案管理系统概览</p>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: #f2f9ff;">
          <a-icon type="folder" style="color: #0075de;" />
        </div>
        <div class="stat-info">
          <div class="stat-value">1,256</div>
          <div class="stat-label">档案总数</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #e6f7e6;">
          <a-icon type="upload" style="color: #1aae39;" />
        </div>
        <div class="stat-info">
          <div class="stat-value">23</div>
          <div class="stat-label">今日上传</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #fff3e6;">
          <a-icon type="file-protect" style="color: #dd5b00;" />
        </div>
        <div class="stat-info">
          <div class="stat-value">8</div>
          <div class="stat-label">待审核</div>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background: #f5e6ff;">
          <a-icon type="user" style="color: #391c57;" />
        </div>
        <div class="stat-info">
          <div class="stat-value">45</div>
          <div class="stat-label">用户数量</div>
        </div>
      </div>
    </div>

    <a-row :gutter="20">
      <a-col :span="16">
        <div class="card-container">
          <h3 style="margin-bottom: 16px;">档案分类统计</h3>
          <div id="chart-category" style="height: 300px;"></div>
        </div>
      </a-col>
      <a-col :span="8">
        <div class="card-container">
          <h3 style="margin-bottom: 16px;">密级分布</h3>
          <div id="chart-security" style="height: 300px;"></div>
        </div>
      </a-col>
    </a-row>

    <div style="margin-top: 20px;">
      <h3 style="margin-bottom: 16px;">快捷操作</h3>
      <a-row :gutter="16">
        <a-col :span="6" v-for="action in quickActions" :key="action.path">
          <div class="action-card" @click="$router.push(action.path)">
            <a-icon :type="action.icon" style="font-size: 32px; color: #0075de;" />
            <span>{{ action.label }}</span>
          </div>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import G2 from '@antv/g2'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters('user', ['currentUser']),
    userInfo() { return this.currentUser }
  },
  data() {
    return {
      quickActions: [
        { icon: 'upload', label: '上传档案', path: '/archive/upload' },
        { icon: 'search', label: '档案查询', path: '/search' },
        { icon: 'folder-open', label: '档案管理', path: '/archive/management' },
        { icon: 'file-protect', label: '查档申请', path: '/application' }
      ]
    }
  },
  mounted() {
    this.renderCategoryChart()
    this.renderSecurityChart()
  },
  methods: {
    renderCategoryChart() {
      const data = [
        { type: '行政文件', value: 350 },
        { type: '人事档案', value: 280 },
        { type: '财务资料', value: 220 },
        { type: '工程项目', value: 260 },
        { type: '其他', value: 146 }
      ]
      const chart = new G2.Chart({ container: 'chart-category', forceFit: true, height: 300 })
      chart.source(data)
      chart.interval().position('type*value').color('type', ['#0075de', '#2a9d99', '#dd5b00', '#391c57', '#a39e98'])
      chart.render()
    },
    renderSecurityChart() {
      const data = [
        { type: '公开', value: 420 },
        { type: '内部', value: 510 },
        { type: '秘密', value: 230 },
        { type: '机密', value: 96 }
      ]
      const chart = new G2.Chart({ container: 'chart-security', forceFit: true, height: 300 })
      chart.source(data)
      chart.coord('theta')
      chart.intervalStack().position('value').color('type', ['#1aae39', '#0075de', '#dd5b00', '#ff4d4f']).style({ lineWidth: 1, stroke: '#fff' })
      chart.render()
    }
  }
}
</script>

<style lang="less" scoped>
.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 32px;
  background: #fff;
  border: 1px solid var(--color-border);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  &:hover { box-shadow: 0 4px 18px rgba(0,0,0,0.04); transform: translateY(-2px); }
  span { font-size: 14px; font-weight: 500; }
}
</style>
```

- [ ] **Step 2: 验证**

登录后仪表盘显示统计卡片、柱状图、饼图、快捷操作。

- [ ] **Step 3: 提交**

```bash
git add src/views/Dashboard.vue
git commit -m "feat: 实现首页仪表盘，含AntV G2图表"
```

---

## Task 8: 档案管理页面

**Files:**
- Create: `src/views/archive/Management.vue`

- [ ] **Step 1: 创建档案管理页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>档案管理</h2>
      <p>管理电子档案文件夹和档案文件</p>
    </div>
    <div style="display: flex; gap: 20px; height: calc(100vh - 220px);">
      <!-- 左侧文件夹树 -->
      <div style="width: 240px; flex-shrink: 0;" class="card-container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid var(--color-border);">
          <span style="font-weight: 600;">文件夹</span>
          <a-button type="link" size="small" @click="showCreateFolder = true">
            <a-icon type="plus" />新建
          </a-button>
        </div>
        <a-tree
          :tree-data="folderTree"
          :default-expand-all="true"
          :replace-fields="{ title: 'name', key: 'id' }"
          @select="onFolderSelect"
        >
          <a-icon slot="icon" type="folder" style="color: #a39e98;" />
        </a-tree>
      </div>
      <!-- 右侧档案列表 -->
      <div style="flex: 1;" class="card-container">
        <div class="toolbar">
          <a-input-search
            v-model="keyword"
            placeholder="搜索档案..."
            style="width: 300px;"
            @search="onSearch"
          />
          <a-button type="primary" icon="upload" @click="$router.push('/archive/upload')">上传档案</a-button>
        </div>
        <a-table
          :columns="columns"
          :data-source="archives"
          :pagination="{ current: 1, pageSize: 10, total: 100 }"
          row-key="id"
          size="middle"
        >
          <template slot="securityLevel" slot-scope="text">
            <a-tag :color="getSecurityColor(text)">{{ text }}</a-tag>
          </template>
          <template slot="action" slot-scope="text, record">
            <a-button type="link" size="small" @click="preview(record)">预览</a-button>
            <a-button type="link" size="small" @click="edit(record)">编辑</a-button>
            <a-popconfirm title="确定删除此档案？" @confirm="remove(record)">
              <a-button type="link" size="small" class="danger-text">删除</a-button>
            </a-popconfirm>
          </template>
        </a-table>
      </div>
    </div>

    <a-modal title="新建文件夹" :visible="showCreateFolder" @ok="createFolder" @cancel="showCreateFolder = false">
      <a-input v-model="newFolderName" placeholder="请输入文件夹名称" />
    </a-modal>
  </div>
</template>

<script>
const columns = [
  { title: '档案标题', dataIndex: 'title', key: 'title' },
  { title: '类别', dataIndex: 'category', key: 'category' },
  { title: '密级', dataIndex: 'securityLevel', key: 'securityLevel', scopedSlots: { customRender: 'securityLevel' } },
  { title: '所属部门', dataIndex: 'department', key: 'department' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', scopedSlots: { customRender: 'action' }, width: 200 }
]
export default {
  name: 'ArchiveManagement',
  data() {
    return {
      keyword: '',
      showCreateFolder: false,
      newFolderName: '',
      columns,
      folderTree: [
        { id: 1, name: '综合处室', children: [
          { id: 11, name: '行政文件' },
          { id: 12, name: '人事档案' },
          { id: 13, name: '财务资料' }
        ]},
        { id: 2, name: '项目档案', children: [
          { id: 21, name: '工程项目' },
          { id: 22, name: '采购项目' }
        ]}
      ],
      archives: [
        { id: 1, title: '2024年度工作总结', category: '行政文件', securityLevel: '内部', department: '综合处', createdAt: '2024-12-20' },
        { id: 2, title: '人事任免通知', category: '人事档案', securityLevel: '秘密', department: '人事处', createdAt: '2024-12-19' },
        { id: 3, title: '季度财务报表', category: '财务资料', securityLevel: '机密', department: '财务处', createdAt: '2024-12-18' },
        { id: 4, title: '水务工程验收报告', category: '工程项目', securityLevel: '内部', department: '工程处', createdAt: '2024-12-17' },
        { id: 5, title: '会议纪要', category: '行政文件', securityLevel: '公开', department: '综合处', createdAt: '2024-12-16' }
      ]
    }
  },
  methods: {
    getSecurityColor(level) {
      const map = { '公开': 'green', '内部': 'blue', '秘密': 'orange', '机密': 'red' }
      return map[level] || ''
    },
    onFolderSelect(keys) { console.log('选择文件夹:', keys) },
    onSearch(val) { console.log('搜索:', val) },
    preview(record) { this.$router.push(`/reader/${record.id}`) },
    edit(record) { console.log('编辑:', record) },
    remove(record) { this.$message.success('删除成功') },
    createFolder() {
      if (!this.newFolderName) return this.$warning({ title: '请输入文件夹名称' })
      this.$message.success('文件夹创建成功')
      this.showCreateFolder = false
      this.newFolderName = ''
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

文件夹树、档案表格、搜索、密级标签颜色均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/archive/Management.vue
git commit -m "feat: 实现档案管理页面"
```

---

## Task 9: 档案上传页面

**Files:**
- Create: `src/views/archive/Upload.vue`

- [ ] **Step 1: 创建档案上传页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>档案上传</h2>
      <p>上传电子档案文件到系统</p>
    </div>
    <div class="card-container" style="max-width: 700px;">
      <a-form-model
        ref="form"
        :model="formData"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-model-item label="档案标题" prop="title">
          <a-input v-model="formData.title" placeholder="请输入档案标题" />
        </a-form-model-item>
        <a-form-model-item label="所属文件夹" prop="folderId">
          <a-tree-select
            v-model="formData.folderId"
            :tree-data="folderOptions"
            :replace-fields="{ title: 'name', value: 'id', key: 'id' }"
            placeholder="请选择文件夹"
            tree-default-expand-all
          />
        </a-form-model-item>
        <a-form-model-item label="档案类别" prop="categoryId">
          <a-select v-model="formData.categoryId" placeholder="请选择档案类别">
            <a-select-option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="密级等级" prop="securityLevelId">
          <a-select v-model="formData.securityLevelId" placeholder="请选择密级等级">
            <a-select-option v-for="s in securityLevels" :key="s.id" :value="s.id">{{ s.name }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="上传文件" prop="files">
          <a-upload-dragger
            :file-list="formData.files"
            :before-upload="beforeUpload"
            :remove="handleRemove"
            multiple
          >
            <p class="ant-upload-drag-icon"><a-icon type="inbox" /></p>
            <p class="ant-upload-text">将文件拖到此处，或点击上传</p>
            <p class="ant-upload-hint">支持 OFFICE、PDF、图片等格式，可批量上传</p>
          </a-upload-dragger>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 16, offset: 6 }">
          <a-button type="primary" :loading="submitting" @click="handleSubmit">开始上传</a-button>
          <a-button style="margin-left: 12px;" @click="resetForm">重置</a-button>
        </a-form-model-item>
      </a-form-model>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArchiveUpload',
  data() {
    return {
      formData: { title: '', folderId: undefined, categoryId: undefined, securityLevelId: undefined, files: [] },
      rules: {
        title: [{ required: true, message: '请输入档案标题', trigger: 'blur' }],
        folderId: [{ required: true, message: '请选择文件夹', trigger: 'change' }],
        categoryId: [{ required: true, message: '请选择类别', trigger: 'change' }],
        securityLevelId: [{ required: true, message: '请选择密级', trigger: 'change' }]
      },
      folderOptions: [
        { id: 1, name: '综合处室', children: [{ id: 11, name: '行政文件' }, { id: 12, name: '人事档案' }] }
      ],
      categories: [
        { id: 1, name: '行政文件' }, { id: 2, name: '人事档案' }, { id: 3, name: '财务资料' }, { id: 4, name: '工程项目' }
      ],
      securityLevels: [
        { id: 1, name: '公开' }, { id: 2, name: '内部' }, { id: 3, name: '秘密' }, { id: 4, name: '机密' }
      ],
      submitting: false
    }
  },
  methods: {
    beforeUpload(file) {
      this.formData.files = [...this.formData.files, file]
      return false
    },
    handleRemove(file) {
      this.formData.files = this.formData.files.filter(f => f.uid !== file.uid)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.submitting = true
        setTimeout(() => {
          this.$message.success('档案上传成功')
          this.submitting = false
          this.resetForm()
        }, 2000)
      })
    },
    resetForm() { this.$refs.form.resetFields(); this.formData.files = [] }
  }
}
</script>
```

- [ ] **Step 2: 验证**

表单验证、树选择、拖拽上传区域均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/archive/Upload.vue
git commit -m "feat: 实现档案上传页面"
```

---

## Task 10: 档案查询页面

**Files:**
- Create: `src/views/search/Search.vue`

- [ ] **Step 1: 创建档案查询页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>档案查询</h2>
      <p>支持全文检索和高级搜索</p>
    </div>
    <div class="card-container" style="margin-bottom: 20px;">
      <a-input-search
        v-model="keyword"
        placeholder="输入关键词搜索档案..."
        size="large"
        enter-button="搜索"
        @search="doSearch"
      />
      <div style="text-align: center; margin-top: 12px;">
        <a-button type="link" @click="showAdvanced = !showAdvanced">
          {{ showAdvanced ? '收起高级搜索' : '展开高级搜索' }}
          <a-icon :type="showAdvanced ? 'up' : 'down'" />
        </a-button>
      </div>
      <a-collapse v-if="showAdvanced" :bordered="false" style="margin-top: 16px;">
        <a-collapse-panel key="1" header="高级搜索条件">
          <a-form-model layout="inline" :model="advancedForm">
            <a-form-model-item label="工程名称"><a-input v-model="advancedForm.projectName" /></a-form-model-item>
            <a-form-model-item label="档案号"><a-input v-model="advancedForm.archiveNo" /></a-form-model-item>
            <a-form-model-item label="创建人"><a-input v-model="advancedForm.creator" /></a-form-model-item>
            <a-form-model-item label="创建时间">
              <a-range-picker v-model="advancedForm.dateRange" />
            </a-form-model-item>
            <a-form-model-item label="密级">
              <a-select v-model="advancedForm.securityLevel" style="width: 120px;" allow-clear>
                <a-select-option value="公开">公开</a-select-option>
                <a-select-option value="内部">内部</a-select-option>
                <a-select-option value="秘密">秘密</a-select-option>
                <a-select-option value="机密">机密</a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item>
              <a-button type="primary" @click="doSearch">搜索</a-button>
              <a-button style="margin-left: 8px;" @click="resetAdvanced">重置</a-button>
            </a-form-model-item>
          </a-form-model>
        </a-collapse-panel>
      </a-collapse>
    </div>

    <div class="card-container">
      <div style="margin-bottom: 16px; color: var(--color-text-secondary);">
        找到 <strong>{{ results.length }}</strong> 条结果
      </div>
      <a-table :columns="columns" :data-source="results" row-key="id" size="middle">
        <template slot="titleCol" slot-scope="text">
          <a-button type="link" style="padding: 0;" @click="preview(text)">{{ text.title }}</a-button>
        </template>
        <template slot="securityLevel" slot-scope="text">
          <a-tag :color="getSecurityColor(text)">{{ text }}</a-tag>
        </template>
        <template slot="action" slot-scope="text, record">
          <a-button type="link" size="small" @click="preview(record)">预览</a-button>
          <a-button type="link" size="small" @click="applyView(record)">申请查阅</a-button>
        </template>
      </a-table>
    </div>
  </div>
</template>

<script>
const columns = [
  { title: '档案标题', dataIndex: 'title', key: 'title', scopedSlots: { customRender: 'titleCol' } },
  { title: '类别', dataIndex: 'category', key: 'category' },
  { title: '密级', dataIndex: 'securityLevel', key: 'securityLevel', scopedSlots: { customRender: 'securityLevel' } },
  { title: '创建人', dataIndex: 'creator', key: 'creator' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '库房位置', dataIndex: 'location', key: 'location' },
  { title: '操作', key: 'action', scopedSlots: { customRender: 'action' }, width: 150 }
]
export default {
  name: 'ArchiveSearch',
  data() {
    return {
      keyword: '',
      showAdvanced: false,
      columns,
      advancedForm: { projectName: '', archiveNo: '', creator: '', dateRange: null, securityLevel: '' },
      results: [
        { id: 1, title: '2024年度工作总结', category: '行政文件', securityLevel: '内部', creator: '张三', createdAt: '2024-12-20', location: 'A区-01柜' },
        { id: 2, title: '人事任免通知', category: '人事档案', securityLevel: '秘密', creator: '李四', createdAt: '2024-12-19', location: 'B区-03柜' },
        { id: 3, title: '季度财务报表', category: '财务资料', securityLevel: '机密', creator: '王五', createdAt: '2024-12-18', location: 'C区-02柜' },
        { id: 4, title: '水务工程验收报告', category: '工程项目', securityLevel: '内部', creator: '赵六', createdAt: '2024-12-17', location: 'D区-05柜' },
        { id: 5, title: '会议纪要', category: '行政文件', securityLevel: '公开', creator: '张三', createdAt: '2024-12-16', location: 'A区-01柜' }
      ]
    }
  },
  methods: {
    getSecurityColor(level) {
      const map = { '公开': 'green', '内部': 'blue', '秘密': 'orange', '机密': 'red' }
      return map[level] || ''
    },
    doSearch() { this.$message.info('搜索功能待后端实现') },
    resetAdvanced() { this.advancedForm = { projectName: '', archiveNo: '', creator: '', dateRange: null, securityLevel: '' } },
    preview(record) { this.$router.push(`/reader/${record.id}`) },
    applyView(record) {
      this.$confirm({
        title: `确定要申请查阅《${record.title}》吗？`,
        onOk: () => { this.$message.success('申请已提交，请等待审核') }
      })
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

全文搜索、高级搜索展开/收起、结果表格、申请查阅均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/search/Search.vue
git commit -m "feat: 实现档案查询页面"
```

---

## Task 11: 档案阅读页面

**Files:**
- Create: `src/views/reader/Reader.vue`

- [ ] **Step 1: 创建档案阅读页面**

```vue
<template>
  <div style="display: flex; flex-direction: column; height: calc(100vh - 56px);">
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 12px 24px; background: #fff; border-bottom: 1px solid var(--color-border);">
      <div>
        <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 4px;">{{ currentFile.title || '请选择要阅读的档案' }}</h2>
        <div v-if="currentFile.title" style="display: flex; align-items: center; gap: 12px; font-size: 14px; color: var(--color-text-secondary);">
          <a-tag :color="getSecurityColor(currentFile.securityLevel)" size="small">{{ currentFile.securityLevel }}</a-tag>
          <span>{{ currentFile.fileType }}</span>
          <span>{{ currentFile.fileSize }}</span>
        </div>
      </div>
      <div>
        <a-button icon="download" @click="$message.info('下载功能待后端实现')">下载</a-button>
        <a-button icon="printer" style="margin-left: 8px;" @click="$message.info('打印功能待后端实现')">打印</a-button>
      </div>
    </div>
    <div style="display: flex; flex: 1; overflow: hidden;">
      <!-- 左侧文件列表 -->
      <div style="width: 240px; flex-shrink: 0; background: #fff; border-right: 1px solid var(--color-border); overflow: auto;">
        <div style="padding: 16px; font-weight: 600; border-bottom: 1px solid var(--color-border);">文件列表</div>
        <div
          v-for="file in fileList"
          :key="file.id"
          :style="{
            display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 16px', cursor: 'pointer',
            background: file.id === activeFileId ? '#f2f9ff' : 'transparent',
            color: file.id === activeFileId ? '#0075de' : 'inherit'
          }"
          @click="activeFileId = file.id"
        >
          <a-icon :type="getFileIcon(file.fileType)" />
          <span style="font-size: 14px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ file.name }}</span>
        </div>
      </div>
      <!-- 右侧预览 -->
      <div style="flex: 1; background: var(--color-bg-warm); display: flex; align-items: center; justify-content: center;">
        <div v-if="!currentFile.title" style="text-align: center; color: var(--color-text-muted);">
          <a-icon type="read" style="font-size: 64px;" />
          <p style="font-size: 16px; margin-top: 16px;">请从左侧选择文件进行预览</p>
        </div>
        <div v-else style="text-align: center; color: var(--color-text-secondary); padding: 48px;">
          <a-icon type="file-text" style="font-size: 64px; color: var(--color-text-muted);" />
          <p style="font-size: 16px; margin: 16px 0 8px;">{{ currentFile.fileType.toUpperCase() }} 文件预览区域</p>
          <p style="font-size: 14px; color: var(--color-text-muted);">后端API实现后加载真实文件内容</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArchiveReader',
  data() {
    return {
      activeFileId: 1,
      fileList: [
        { id: 1, name: '2024年度工作总结.docx', fileType: 'docx', fileSize: '2.5MB' },
        { id: 2, name: '附件1-数据报表.xlsx', fileType: 'xlsx', fileSize: '1.2MB' },
        { id: 3, name: '附件2-现场照片.jpg', fileType: 'jpg', fileSize: '3.8MB' }
      ],
      files: {
        1: { title: '2024年度工作总结', securityLevel: '内部', fileType: 'docx', fileSize: '2.5MB' },
        2: { title: '附件1-数据报表', securityLevel: '公开', fileType: 'xlsx', fileSize: '1.2MB' },
        3: { title: '附件2-现场照片', securityLevel: '公开', fileType: 'jpg', fileSize: '3.8MB' }
      }
    }
  },
  computed: {
    currentFile() { return this.files[this.activeFileId] || {} }
  },
  methods: {
    getSecurityColor(level) {
      const map = { '公开': 'green', '内部': 'blue', '秘密': 'orange', '机密': 'red' }
      return map[level] || ''
    },
    getFileIcon(type) {
      const map = { docx: 'file-word', xlsx: 'file-excel', pdf: 'file-pdf', jpg: 'picture', png: 'picture', bmp: 'picture' }
      return map[type] || 'file'
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

文件列表切换、预览区域、下载打印按钮均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/reader/Reader.vue
git commit -m "feat: 实现档案阅读页面"
```

---

## Task 12: 查档管理页面

**Files:**
- Create: `src/views/application/Application.vue`

- [ ] **Step 1: 创建查档管理页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>查档管理</h2>
      <p>管理查档申请和审核流程</p>
    </div>
    <a-tabs v-model="activeTab">
      <a-tab-pane key="myApplications" tab="我的申请">
        <div class="toolbar">
          <a-button type="primary" icon="plus" @click="showApplyModal = true">新建申请</a-button>
        </div>
        <a-table :columns="myColumns" :data-source="myApplications" row-key="id" size="middle">
          <template slot="status" slot-scope="text">
            <a-tag :color="getStatusColor(text)">{{ getStatusText(text) }}</a-tag>
          </template>
        </a-table>
      </a-tab-pane>
      <a-tab-pane v-if="isManager" key="pendingReview" tab="待我审核">
        <a-table :columns="reviewColumns" :data-source="pendingReviews" row-key="id" size="middle">
          <template slot="action" slot-scope="text, record">
            <a-button type="link" size="small" style="color: #52c41a;" @click="openReview(record, 'approve')">通过</a-button>
            <a-button type="link" size="small" class="danger-text" @click="openReview(record, 'reject')">拒绝</a-button>
          </template>
        </a-table>
      </a-tab-pane>
    </a-tabs>

    <a-modal title="新建查档申请" :visible="showApplyModal" @ok="submitApplication" @cancel="showApplyModal = false">
      <a-form-model ref="applyForm" :model="applyForm" :rules="applyRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-model-item label="档案名称" prop="archiveName">
          <a-input v-model="applyForm.archiveName" placeholder="请输入档案名称" />
        </a-form-model-item>
        <a-form-model-item label="档案类型" prop="archiveType">
          <a-select v-model="applyForm.archiveType" placeholder="请选择">
            <a-select-option value="行政文件">行政文件</a-select-option>
            <a-select-option value="人事档案">人事档案</a-select-option>
            <a-select-option value="财务资料">财务资料</a-select-option>
            <a-select-option value="工程项目">工程项目</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="查询用途" prop="purpose">
          <a-textarea v-model="applyForm.purpose" :rows="3" placeholder="请说明查询用途" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>

    <a-modal :title="reviewAction === 'approve' ? '审核通过' : '审核拒绝'" :visible="showReviewModal" @ok="submitReview" @cancel="showReviewModal = false">
      <a-textarea v-model="reviewComment" :rows="3" placeholder="请输入审核意见" />
    </a-modal>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
const myColumns = [
  { title: '档案名称', dataIndex: 'archiveName', key: 'archiveName' },
  { title: '档案类型', dataIndex: 'archiveType', key: 'archiveType' },
  { title: '查询用途', dataIndex: 'purpose', key: 'purpose' },
  { title: '状态', dataIndex: 'status', key: 'status', scopedSlots: { customRender: 'status' } },
  { title: '申请时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '审核意见', dataIndex: 'reviewComment', key: 'reviewComment' }
]
const reviewColumns = [
  { title: '档案名称', dataIndex: 'archiveName', key: 'archiveName' },
  { title: '申请人', dataIndex: 'applicant', key: 'applicant' },
  { title: '查询用途', dataIndex: 'purpose', key: 'purpose' },
  { title: '申请时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', scopedSlots: { customRender: 'action' }, width: 150 }
]
export default {
  name: 'ApplicationManage',
  data() {
    return {
      activeTab: 'myApplications',
      myColumns, reviewColumns,
      showApplyModal: false,
      showReviewModal: false,
      reviewAction: '',
      reviewComment: '',
      applyForm: { archiveName: '', archiveType: '', purpose: '' },
      applyRules: {
        archiveName: [{ required: true, message: '请输入档案名称', trigger: 'blur' }],
        archiveType: [{ required: true, message: '请选择类型', trigger: 'change' }],
        purpose: [{ required: true, message: '请说明用途', trigger: 'blur' }]
      },
      myApplications: [
        { id: 1, archiveName: '2024年度工作总结', archiveType: '行政文件', purpose: '年度总结参考', status: 0, createdAt: '2024-12-20', reviewComment: '' },
        { id: 2, archiveName: '人事任免通知', archiveType: '人事档案', purpose: '人事变动查询', status: 1, createdAt: '2024-12-19', reviewComment: '同意查阅' },
        { id: 3, archiveName: '项目可行性报告', archiveType: '工程项目', purpose: '项目立项参考', status: 2, createdAt: '2024-12-18', reviewComment: '密级不符' }
      ],
      pendingReviews: [
        { id: 4, archiveName: '季度财务报表', archiveType: '财务资料', purpose: '财务分析', applicant: '王五', createdAt: '2024-12-20' },
        { id: 5, archiveName: '工程验收报告', archiveType: '工程项目', purpose: '验收参考', applicant: '赵六', createdAt: '2024-12-19' }
      ]
    }
  },
  computed: {
    ...mapGetters('user', ['isAdmin', 'isDeptAdmin']),
    isManager() { return this.isAdmin || this.isDeptAdmin }
  },
  methods: {
    getStatusColor(s) { return { 0: 'orange', 1: 'green', 2: 'red' }[s] || '' },
    getStatusText(s) { return { 0: '待审核', 1: '已通过', 2: '已拒绝' }[s] || '' },
    submitApplication() {
      this.$refs.applyForm.validate(valid => {
        if (valid) { this.$message.success('申请已提交'); this.showApplyModal = false; this.$refs.applyForm.resetFields() }
      })
    },
    openReview(record, action) { this.reviewAction = action; this.reviewComment = ''; this.showReviewModal = true },
    submitReview() {
      this.$message.success(this.reviewAction === 'approve' ? '已通过' : '已拒绝')
      this.showReviewModal = false
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

选项卡切换、新建申请、审核弹窗均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/application/Application.vue
git commit -m "feat: 实现查档管理页面"
```

---

## Task 13: 用户管理页面

**Files:**
- Create: `src/views/system/UserManage.vue`

- [ ] **Step 1: 创建用户管理页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>用户管理</h2>
      <p>管理系统用户账号和角色</p>
    </div>
    <div class="card-container">
      <div class="toolbar">
        <a-input-search v-model="keyword" placeholder="搜索用户名/姓名" style="width: 250px;" />
        <a-button type="primary" icon="plus" @click="openAdd">新增用户</a-button>
      </div>
      <a-table :columns="columns" :data-source="filteredUsers" row-key="id" size="middle">
        <template slot="role" slot-scope="text">
          <a-tag :color="getRoleColor(text)">{{ getRoleText(text) }}</a-tag>
        </template>
        <template slot="status" slot-scope="text, record">
          <a-switch :checked="text === 1" @change="val => toggleStatus(record, val)" />
        </template>
        <template slot="action" slot-scope="text, record">
          <a-button type="link" size="small" @click="openEdit(record)">编辑</a-button>
          <a-popconfirm title="确定删除该用户？" @confirm="removeUser(record)">
            <a-button type="link" size="small" class="danger-text">删除</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </div>

    <a-modal :title="isEdit ? '编辑用户' : '新增用户'" :visible="showModal" @ok="submitForm" @cancel="showModal = false" width="500px">
      <a-form-model ref="userForm" :model="userForm" :rules="userRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-model-item label="用户名" prop="username">
          <a-input v-model="userForm.username" :disabled="isEdit" />
        </a-form-model-item>
        <a-form-model-item v-if="!isEdit" label="密码" prop="password">
          <a-input-password v-model="userForm.password" />
        </a-form-model-item>
        <a-form-model-item label="姓名" prop="realName">
          <a-input v-model="userForm.realName" />
        </a-form-model-item>
        <a-form-model-item label="所属部门" prop="departmentId">
          <a-select v-model="userForm.departmentId" placeholder="请选择">
            <a-select-option v-for="d in departments" :key="d.id" :value="d.id">{{ d.name }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="角色" prop="role">
          <a-select v-model="userForm.role" placeholder="请选择">
            <a-select-option value="admin">系统管理员</a-select-option>
            <a-select-option value="dept_admin">部门管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
const columns = [
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '姓名', dataIndex: 'realName', key: 'realName' },
  { title: '所属部门', dataIndex: 'department', key: 'department' },
  { title: '角色', dataIndex: 'role', key: 'role', scopedSlots: { customRender: 'role' } },
  { title: '状态', dataIndex: 'status', key: 'status', scopedSlots: { customRender: 'status' } },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', scopedSlots: { customRender: 'action' }, width: 150 }
]
export default {
  name: 'UserManage',
  data() {
    return {
      keyword: '',
      columns,
      showModal: false,
      isEdit: false,
      userForm: { username: '', password: '', realName: '', departmentId: undefined, role: '' },
      userRules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '不少于6位' }],
        realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        departmentId: [{ required: true, message: '请选择部门', trigger: 'change' }],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }]
      },
      departments: [
        { id: 1, name: '综合处' }, { id: 2, name: '人事处' }, { id: 3, name: '财务处' }, { id: 4, name: '工程处' }
      ],
      users: [
        { id: 1, username: 'admin', realName: '系统管理员', department: '综合处', role: 'admin', status: 1, createdAt: '2024-01-01' },
        { id: 2, username: 'zhangsan', realName: '张三', department: '综合处', role: 'dept_admin', status: 1, createdAt: '2024-06-15' },
        { id: 3, username: 'lisi', realName: '李四', department: '人事处', role: 'user', status: 1, createdAt: '2024-08-20' },
        { id: 4, username: 'wangwu', realName: '王五', department: '财务处', role: 'user', status: 1, createdAt: '2024-10-10' },
        { id: 5, username: 'zhaoliu', realName: '赵六', department: '工程处', role: 'user', status: 0, createdAt: '2024-11-05' }
      ]
    }
  },
  computed: {
    filteredUsers() {
      if (!this.keyword) return this.users
      const k = this.keyword.toLowerCase()
      return this.users.filter(u => u.username.toLowerCase().includes(k) || u.realName.toLowerCase().includes(k))
    }
  },
  methods: {
    getRoleColor(role) { return { admin: 'red', dept_admin: 'orange', user: 'blue' }[role] || '' },
    getRoleText(role) { return { admin: '系统管理员', dept_admin: '部门管理员', user: '普通用户' }[role] || role },
    openAdd() { this.isEdit = false; this.userForm = { username: '', password: '', realName: '', departmentId: undefined, role: '' }; this.showModal = true },
    openEdit(record) { this.isEdit = true; this.userForm = { ...record }; this.showModal = true },
    toggleStatus(record, val) { record.status = val ? 1 : 0; this.$message.success(val ? '已启用' : '已禁用') },
    removeUser(record) { this.$message.success('已删除 ' + record.realName) },
    submitForm() {
      this.$refs.userForm.validate(valid => {
        if (valid) { this.$message.success(this.isEdit ? '修改成功' : '添加成功'); this.showModal = false }
      })
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

用户列表、新增/编辑弹窗、搜索、状态开关均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/system/UserManage.vue
git commit -m "feat: 实现用户管理页面"
```

---

## Task 14: 部门管理页面

**Files:**
- Create: `src/views/system/DeptManage.vue`

- [ ] **Step 1: 创建部门管理页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>部门管理</h2>
      <p>管理公司部门组织结构</p>
    </div>
    <div class="card-container">
      <div class="toolbar">
        <a-button type="primary" icon="plus" @click="openAdd">新增部门</a-button>
      </div>
      <a-table :columns="columns" :data-source="departments" row-key="id" size="middle">
        <template slot="action" slot-scope="text, record">
          <a-button type="link" size="small" @click="openEdit(record)">编辑</a-button>
          <a-popconfirm title="确定删除此部门？" @confirm="remove(record)">
            <a-button type="link" size="small" class="danger-text">删除</a-button>
          </a-popconfirm>
        </template>
      </a-table>
    </div>
    <a-modal :title="isEdit ? '编辑部门' : '新增部门'" :visible="showModal" @ok="submitForm" @cancel="showModal = false" width="500px">
      <a-form-model ref="deptForm" :model="deptForm" :rules="deptRules" :label-col="{ span: 6 }" :wrapper-col="{ span: 16 }">
        <a-form-model-item label="部门名称" prop="name">
          <a-input v-model="deptForm.name" />
        </a-form-model-item>
        <a-form-model-item label="部门描述">
          <a-textarea v-model="deptForm.description" :rows="3" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
const columns = [
  { title: '部门名称', dataIndex: 'name', key: 'name' },
  { title: '部门描述', dataIndex: 'description', key: 'description' },
  { title: '用户数量', dataIndex: 'userCount', key: 'userCount' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action', scopedSlots: { customRender: 'action' }, width: 150 }
]
export default {
  name: 'DeptManage',
  data() {
    return {
      columns, showModal: false, isEdit: false,
      deptForm: { name: '', description: '' },
      deptRules: { name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] },
      departments: [
        { id: 1, name: '综合处', description: '负责公司综合行政事务', userCount: 15, createdAt: '2024-01-01' },
        { id: 2, name: '人事处', description: '负责公司人力资源管理', userCount: 8, createdAt: '2024-01-01' },
        { id: 3, name: '财务处', description: '负责公司财务管理', userCount: 10, createdAt: '2024-01-01' },
        { id: 4, name: '工程处', description: '负责水务工程项目管理', userCount: 12, createdAt: '2024-01-01' }
      ]
    }
  },
  methods: {
    openAdd() { this.isEdit = false; this.deptForm = { name: '', description: '' }; this.showModal = true },
    openEdit(record) { this.isEdit = true; this.deptForm = { ...record }; this.showModal = true },
    remove(record) { this.$message.success('已删除 ' + record.name) },
    submitForm() {
      this.$refs.deptForm.validate(valid => {
        if (valid) { this.$message.success(this.isEdit ? '修改成功' : '添加成功'); this.showModal = false }
      })
    }
  }
}
</script>
```

- [ ] **Step 2: 验证**

部门表格、新增/编辑弹窗、删除均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/system/DeptManage.vue
git commit -m "feat: 实现部门管理页面"
```

---

## Task 15: 操作日志页面

**Files:**
- Create: `src/views/system/LogManage.vue`

- [ ] **Step 1: 创建操作日志页面**

```vue
<template>
  <div class="page-container">
    <div class="page-header">
      <h2>操作日志</h2>
      <p>查看系统操作记录，追源溯底</p>
    </div>
    <div class="card-container">
      <a-form-model layout="inline" :model="filterForm" style="margin-bottom: 16px;">
        <a-form-model-item label="操作人">
          <a-input v-model="filterForm.operator" style="width: 150px;" allow-clear />
        </a-form-model-item>
        <a-form-model-item label="操作类型">
          <a-select v-model="filterForm.operationType" style="width: 150px;" allow-clear>
            <a-select-option value="upload">上传</a-select-option>
            <a-select-option value="download">下载</a-select-option>
            <a-select-option value="edit">编辑</a-select-option>
            <a-select-option value="delete">删除</a-select-option>
            <a-select-option value="view">查看</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="操作时间">
          <a-range-picker v-model="filterForm.dateRange" />
        </a-form-model-item>
        <a-form-model-item>
          <a-button type="primary" icon="search" @click="$message.info('查询功能待后端实现')">查询</a-button>
          <a-button style="margin-left: 8px;" @click="filterForm = { operator: '', operationType: '', dateRange: null }">重置</a-button>
        </a-form-model-item>
      </a-form-model>
      <a-table :columns="columns" :data-source="logs" row-key="id" size="middle">
        <template slot="operationType" slot-scope="text">
          <a-tag :color="getOpColor(text)">{{ getOpText(text) }}</a-tag>
        </template>
      </a-table>
      <div class="pagination-wrapper">
        <a-pagination :current="1" :page-size="10" :total="50" />
      </div>
    </div>
  </div>
</template>

<script>
const columns = [
  { title: '操作人', dataIndex: 'operator', key: 'operator' },
  { title: '操作类型', dataIndex: 'operationType', key: 'operationType', scopedSlots: { customRender: 'operationType' } },
  { title: '档案名称', dataIndex: 'archiveName', key: 'archiveName' },
  { title: '操作详情', dataIndex: 'details', key: 'details' },
  { title: 'IP地址', dataIndex: 'ip', key: 'ip' },
  { title: '操作时间', dataIndex: 'operationTime', key: 'operationTime' }
]
export default {
  name: 'LogManage',
  data() {
    return {
      columns,
      filterForm: { operator: '', operationType: '', dateRange: null },
      logs: [
        { id: 1, operator: '张三', operationType: 'upload', archiveName: '2024年度工作总结', details: '上传文件 2024年度工作总结.docx', ip: '192.168.1.100', operationTime: '2024-12-20 14:30:00' },
        { id: 2, operator: '李四', operationType: 'view', archiveName: '人事任免通知', details: '查看档案', ip: '192.168.1.101', operationTime: '2024-12-20 13:15:00' },
        { id: 3, operator: '王五', operationType: 'download', archiveName: '季度财务报表', details: '下载文件', ip: '192.168.1.102', operationTime: '2024-12-20 11:20:00' },
        { id: 4, operator: '赵六', operationType: 'edit', archiveName: '水务工程验收报告', details: '修改档案标题', ip: '192.168.1.103', operationTime: '2024-12-20 10:05:00' },
        { id: 5, operator: '张三', operationType: 'delete', archiveName: '旧会议纪要', details: '删除档案', ip: '192.168.1.100', operationTime: '2024-12-19 16:45:00' }
      ]
    }
  },
  methods: {
    getOpColor(type) { return { upload: 'green', download: 'blue', edit: 'orange', delete: 'red', view: 'cyan' }[type] || '' },
    getOpText(type) { return { upload: '上传', download: '下载', edit: '编辑', delete: '删除', view: '查看' }[type] || type }
  }
}
</script>
```

- [ ] **Step 2: 验证**

日志表格、筛选表单、操作类型标签均正常。

- [ ] **Step 3: 提交**

```bash
git add src/views/system/LogManage.vue
git commit -m "feat: 实现操作日志页面"
```

---

## Task 16: 最终验证

**Files:** None

- [ ] **Step 1: 启动并全面验证**

```bash
npm run serve
```

逐项检查：
1. 登录页 → 输入用户名密码 → 跳转首页
2. 首页仪表盘 → 统计卡片 + AntV图表 + 快捷操作
3. 左侧菜单 → 折叠/展开 → 各页面路由跳转
4. 档案管理 → 文件夹树 + 档案表格 + 搜索
5. 档案上传 → 表单验证 + 拖拽上传
6. 档案查询 → 全文搜索 + 高级搜索
7. 档案阅读 → 文件列表 + 预览区域
8. 查档管理 → 我的申请 + 待审核
9. 系统管理 → 用户/部门/日志（仅管理员可见）
10. 退出登录 → 跳转登录页

- [ ] **Step 2: 最终提交**

```bash
git add .
git commit -m "feat: 完成前端界面全部开发，Vue2+AntDesignVue+AntV"
```

---

## Plan Self-Review

1. **Spec coverage:** 所有功能页面已覆盖：登录、仪表盘、档案管理、上传、查询、阅读、查档管理、用户管理、部门管理、操作日志
2. **Placeholder scan:** 无TBD/TODO，所有步骤含完整代码
3. **Type consistency:** 组件名、路由路径、API命名保持一致，全部使用Ant Design Vue组件
