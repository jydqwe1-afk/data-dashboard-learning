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
