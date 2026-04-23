<template>
  <div class="page-container">
    <div class="page-header">
      <h2>档案上传</h2>
      <p>上传电子档案文件到系统</p>
    </div>
    <div class="card-container" style="max-width: 700px;">
      <a-form-model
        ref="form"
        :model="formData"
        :rules="rules"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 16 }"
      >
        <a-form-model-item label="档案标题" prop="title">
          <a-input v-model="formData.title" placeholder="请输入档案标题" />
        </a-form-model-item>
        <a-form-model-item label="所属文件夹" prop="folderId">
          <a-tree-select
            v-model="formData.folderId"
            :tree-data="folderOptions"
            :replace-fields="{ title: 'name', value: 'id', key: 'id' }"
            placeholder="请选择文件夹"
            tree-default-expand-all
          />
        </a-form-model-item>
        <a-form-model-item label="档案类别" prop="categoryId">
          <a-select v-model="formData.categoryId" placeholder="请选择档案类别">
            <a-select-option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="密级等级" prop="securityLevelId">
          <a-select v-model="formData.securityLevelId" placeholder="请选择密级等级">
            <a-select-option v-for="s in securityLevels" :key="s.id" :value="s.id">{{ s.name }}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="上传文件" prop="files">
          <a-upload-dragger
            :file-list="formData.files"
            :before-upload="beforeUpload"
            :remove="handleRemove"
            multiple
          >
            <p class="ant-upload-drag-icon"><a-icon type="inbox" /></p>
            <p class="ant-upload-text">将文件拖到此处，或点击上传</p>
            <p class="ant-upload-hint">支持 OFFICE、PDF、图片等格式，可批量上传</p>
          </a-upload-dragger>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 16, offset: 6 }">
          <a-button type="primary" :loading="submitting" @click="handleSubmit">开始上传</a-button>
          <a-button style="margin-left: 12px;" @click="resetForm">重置</a-button>
        </a-form-model-item>
      </a-form-model>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArchiveUpload',
  data() {
    return {
      formData: { title: '', folderId: undefined, categoryId: undefined, securityLevelId: undefined, files: [] },
      rules: {
        title: [{ required: true, message: '请输入档案标题', trigger: 'blur' }],
        folderId: [{ required: true, message: '请选择文件夹', trigger: 'change' }],
        categoryId: [{ required: true, message: '请选择类别', trigger: 'change' }],
        securityLevelId: [{ required: true, message: '请选择密级', trigger: 'change' }]
      },
      folderOptions: [
        { id: 1, name: '综合处室', children: [{ id: 11, name: '行政文件' }, { id: 12, name: '人事档案' }] }
      ],
      categories: [
        { id: 1, name: '行政文件' }, { id: 2, name: '人事档案' }, { id: 3, name: '财务资料' }, { id: 4, name: '工程项目' }
      ],
      securityLevels: [
        { id: 1, name: '公开' }, { id: 2, name: '内部' }, { id: 3, name: '秘密' }, { id: 4, name: '机密' }
      ],
      submitting: false
    }
  },
  methods: {
    beforeUpload(file) {
      this.formData.files = [...this.formData.files, file]
      return false
    },
    handleRemove(file) {
      this.formData.files = this.formData.files.filter(f => f.uid !== file.uid)
    },
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (!valid) return
        this.submitting = true
        setTimeout(() => {
          this.$message.success('档案上传成功')
          this.submitting = false
          this.resetForm()
        }, 2000)
      })
    },
    resetForm() { this.$refs.form.resetFields(); this.formData.files = [] }
  }
}
</script>
