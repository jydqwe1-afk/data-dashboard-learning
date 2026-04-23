import http from './http'

export function getFolderTree() {
  return http.get('/archive/folders')
}

export function createFolder(data) {
  return http.post('/archive/folders', data)
}

export function getArchiveList(params) {
  return http.get('/archives', { params })
}

export function getArchiveDetail(id) {
  return http.get(`/archives/${id}`)
}

export function createArchive(data) {
  return http.post('/archives', data)
}

export function updateArchive(id, data) {
  return http.put(`/archives/${id}`, data)
}

export function deleteArchive(id) {
  return http.delete(`/archives/${id}`)
}

export function searchArchives(params) {
  return http.get('/archives/search', { params })
}

export function uploadFile(formData) {
  return http.post('/files/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function downloadFile(fileId) {
  return http.get(`/files/${fileId}/download`, { responseType: 'blob' })
}

export function getCategories() {
  return http.get('/archive/categories')
}

export function getSecurityLevels() {
  return http.get('/archive/security-levels')
}
