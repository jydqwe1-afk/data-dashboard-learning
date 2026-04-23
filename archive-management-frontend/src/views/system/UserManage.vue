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
