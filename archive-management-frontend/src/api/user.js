import http from './http'

export function login(data) {
  return http.post('/auth/login', data)
}

export function getUserInfo() {
  return http.get('/auth/info')
}

export function getUserList(params) {
  return http.get('/system/users', { params })
}

export function createUser(data) {
  return http.post('/system/users', data)
}

export function updateUser(id, data) {
  return http.put(`/system/users/${id}`, data)
}

export function deleteUser(id) {
  return http.delete(`/system/users/${id}`)
}
