import axios from 'axios'

const baseURL = 'http://localhost:8081/'

const axiosInst = axios.create({
  baseURL: baseURL,
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
  },
})

axiosInst.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    console.error('[api.js] API ERROR:', error.response || error.message)
    return Promise.reject(error.response || error.message)
  },
)

const api = {
  get: (url, params = {}, config = {}) => axiosInst.get(url, { params, ...config }),

  post: (url, data = {}, config = {}) => axiosInst.post(url, data, config),

  put: (url, data = {}, config = {}) => axiosInst.put(url, data, config),

  delete: (url, config = {}) => axiosInst.delete(url, config),
}

export default api
