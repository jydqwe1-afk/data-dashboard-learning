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
