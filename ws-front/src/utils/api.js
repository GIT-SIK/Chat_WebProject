import axios from 'axios'

const baseURL = 'http://localhost:8081/api'

const axiosInst = axios.create({
  baseURL: baseURL,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
})

/* config 헤더에 토큰 추가 

SkipAuth 값에 따른 헤더 설정
*/
axiosInst.interceptors.request.use((config) => {
  if (config.skipAuth) {
    return config
  }

  const token = localStorage.getItem('access_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

axiosInst.interceptors.response.use(
  (response) => response,
  (error) => {
    // console.error('[api.js] API ERROR:', error.response || error.message)
    return Promise.reject(error.response || error.message)
  },
)

const api = {
  get: (url, config = {}) => axiosInst.get(url, config),
  post: (url, data = {}, config = {}) => axiosInst.post(url, data, config),
  put: (url, data = {}, config = {}) => axiosInst.put(url, data, config),
  delete: (url, config = {}) => axiosInst.delete(url, config),
}

export default api
