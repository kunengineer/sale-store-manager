import axiosInstance from '../axiosConfig'

const ORDER_API = '/orders'

export const getOpenOrderByTable = async (tableId) => {
  return await axiosInstance.get(`${ORDER_API}/table/${tableId}`)
}

// POST /orders — tạo order mới
export const createOrder = async (payload) => {
  return await axiosInstance.post(ORDER_API, payload)
}

// POST /orders/:orderId/items — thêm items vào order đã có
export const addItemsToOrder = async (orderId, items) => {
  return await axiosInstance.post(`${ORDER_API}/${orderId}/items`, items)
}