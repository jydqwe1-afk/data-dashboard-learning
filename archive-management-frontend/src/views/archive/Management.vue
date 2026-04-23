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
