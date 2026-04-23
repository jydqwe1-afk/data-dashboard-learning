import axios from 'axios'
import store from '@/store'
import router from '@/router'
import { message } from 'ant-design-vue'

const http = axios.create({
  baseURL: '/api',
  timeout: 30000
})

http.interceptors.request.use(config => {
  const token = store.state.user.token
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

http.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      message.error(res.message || '请求失败')
      if (res.code === 401) {
        store.dispatch('user/logout')
        router.push('/login')
      }
      return Promise.reject(new Error(res.message))
    }
    return res.data
  },
  error => {
    message.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default http
