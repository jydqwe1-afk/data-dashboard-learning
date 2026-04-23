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
