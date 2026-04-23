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
