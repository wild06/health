import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  withCredentials: true,
})

// 用户
export const register = (data) => api.post('/users/register', data)
export const login = (data) => api.post('/users/login', data)
export const logout = () => api.get('/users/logout')
export const getUser = (id) => api.get(`/users/${id}`)

// 设备
export const addDevice = (data) => api.post('/devices', data)
export const listDevicesByUser = (userId) => api.get(`/devices/user/${userId}`)
export const connectDevice = (id) => api.put(`/devices/${id}/connect`)
export const disconnectDevice = (id) => api.put(`/devices/${id}/disconnect`)

// 健康数据
export const uploadHealthData = (data) => api.post('/health/data', data)
export const latestHealth = (deviceId) => api.get(`/health/latest/${deviceId}`)
export const historyHealth = (params) => api.get('/health/history', { params })
export const latestByType = (deviceId, dataType) => api.get(`/health/latest/${deviceId}/type/${dataType}`)

// 预警
export const listAlertsByUser = (userId) => api.get(`/alerts/user/${userId}`)
export const markAlertRead = (id) => api.put(`/alerts/${id}/read`)

// 位置
export const uploadLocation = (data) => api.post('/location', data)
export const latestLocation = (deviceId) => api.get(`/location/latest/${deviceId}`)
export const historyLocation = (params) => api.get('/location/history', { params })

export default api
