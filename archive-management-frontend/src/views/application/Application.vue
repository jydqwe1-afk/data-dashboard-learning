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
