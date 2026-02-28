import axios from 'axios'

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/api/v1', // thay đúng context-path BE
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
})

// Request interceptor - sau này thêm token vào đây
axiosInstance.interceptors.request.use(
  (config) => {
    // TODO: thêm token sau
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor - xử lý lỗi chung
axiosInstance.interceptors.response.use(
  (response) => {
    // BE luôn trả về { success, message, data, error, path }
    // tự động unwrap lấy .data bên trong
    return response.data ?? response
  },
  // axiosConfig.js — sửa phần response error
  (error) => {
    const data = error.response?.data
    return Promise.reject({
      status: error.response?.status,
      message: data?.message || 'Có lỗi xảy ra',
      errors: data?.errors ?? [],   // ← giữ lại errors array
    })
  }
)

export default axiosInstance