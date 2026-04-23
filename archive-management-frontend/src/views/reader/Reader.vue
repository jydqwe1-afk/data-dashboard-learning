<template>
  <div style="display: flex; flex-direction: column; height: calc(100vh - 56px);">
    <div style="display: flex; justify-content: space-between; align-items: center; padding: 12px 24px; background: #fff; border-bottom: 1px solid var(--color-border);">
      <div>
        <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 4px;">{{ currentFile.title || '请选择要阅读的档案' }}</h2>
        <div v-if="currentFile.title" style="display: flex; align-items: center; gap: 12px; font-size: 14px; color: var(--color-text-secondary);">
          <a-tag :color="getSecurityColor(currentFile.securityLevel)" size="small">{{ currentFile.securityLevel }}</a-tag>
          <span>{{ currentFile.fileType }}</span>
          <span>{{ currentFile.fileSize }}</span>
        </div>
      </div>
      <div>
        <a-button icon="download" @click="$message.info('下载功能待后端实现')">下载</a-button>
        <a-button icon="printer" style="margin-left: 8px;" @click="$message.info('打印功能待后端实现')">打印</a-button>
      </div>
    </div>
    <div style="display: flex; flex: 1; overflow: hidden;">
      <!-- 左侧文件列表 -->
      <div style="width: 240px; flex-shrink: 0; background: #fff; border-right: 1px solid var(--color-border); overflow: auto;">
        <div style="padding: 16px; font-weight: 600; border-bottom: 1px solid var(--color-border);">文件列表</div>
        <div
          v-for="file in fileList"
          :key="file.id"
          :style="{
            display: 'flex', alignItems: 'center', gap: '8px', padding: '12px 16px', cursor: 'pointer',
            background: file.id === activeFileId ? '#f2f9ff' : 'transparent',
            color: file.id === activeFileId ? '#0075de' : 'inherit'
          }"
          @click="activeFileId = file.id"
        >
          <a-icon :type="getFileIcon(file.fileType)" />
          <span style="font-size: 14px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">{{ file.name }}</span>
        </div>
      </div>
      <!-- 右侧预览 -->
      <div style="flex: 1; background: var(--color-bg-warm); display: flex; align-items: center; justify-content: center;">
        <div v-if="!currentFile.title" style="text-align: center; color: var(--color-text-muted);">
          <a-icon type="read" style="font-size: 64px;" />
          <p style="font-size: 16px; margin-top: 16px;">请从左侧选择文件进行预览</p>
        </div>
        <div v-else style="text-align: center; color: var(--color-text-secondary); padding: 48px;">
          <a-icon type="file-text" style="font-size: 64px; color: var(--color-text-muted);" />
          <p style="font-size: 16px; margin: 16px 0 8px;">{{ currentFile.fileType.toUpperCase() }} 文件预览区域</p>
          <p style="font-size: 14px; color: var(--color-text-muted);">后端API实现后加载真实文件内容</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ArchiveReader',
  data() {
    return {
      activeFileId: 1,
      fileList: [
        { id: 1, name: '2024年度工作总结.docx', fileType: 'docx', fileSize: '2.5MB' },
        { id: 2, name: '附件1-数据报表.xlsx', fileType: 'xlsx', fileSize: '1.2MB' },
        { id: 3, name: '附件2-现场照片.jpg', fileType: 'jpg', fileSize: '3.8MB' }
      ],
      files: {
        1: { title: '2024年度工作总结', securityLevel: '内部', fileType: 'docx', fileSize: '2.5MB' },
        2: { title: '附件1-数据报表', securityLevel: '公开', fileType: 'xlsx', fileSize: '1.2MB' },
        3: { title: '附件2-现场照片', securityLevel: '公开', fileType: 'jpg', fileSize: '3.8MB' }
      }
    }
  },
  computed: {
    currentFile() { return this.files[this.activeFileId] || {} }
  },
  methods: {
    getSecurityColor(level) {
      const map = { '公开': 'green', '内部': 'blue', '秘密': 'orange', '机密': 'red' }
      return map[level] || ''
    },
    getFileIcon(type) {
      const map = { docx: 'file-word', xlsx: 'file-excel', pdf: 'file-pdf', jpg: 'picture', png: 'picture', bmp: 'picture' }
      return map[type] || 'file'
    }
  }
}
</script>
