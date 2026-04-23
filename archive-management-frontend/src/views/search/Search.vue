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
