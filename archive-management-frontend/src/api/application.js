import http from './http'

export function getMyApplications(params) {
  return http.get('/applications/mine', { params })
}

export function createApplication(data) {
  return http.post('/applications', data)
}

export function getPendingReviews(params) {
  return http.get('/applications/reviews', { params })
}

export function reviewApplication(id, data) {
  return http.put(`/applications/${id}/review`, data)
}
