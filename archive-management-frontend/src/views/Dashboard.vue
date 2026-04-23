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
