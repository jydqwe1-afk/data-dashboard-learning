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
