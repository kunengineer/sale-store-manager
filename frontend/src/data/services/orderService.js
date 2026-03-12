import axiosInstance from '../axiosConfig'

const ORDER_API = '/orders'
const ORDER_ITEM_API = '/order-items'

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

export const getOrders = async ({
  page = 1,
  size = 20,
  storeId,
  orderNumber,
  customerId,
  status,
  fromDate,
  toDate,
  minTotal,
  maxTotal,
} = {}) => {
  const params = { page, size }
  if (storeId)                                           params.storeId     = storeId
  if (orderNumber?.trim())                               params.orderNumber  = orderNumber.trim()
  if (customerId)                                        params.customerId   = customerId
  if (status)                                            params.status       = status
  // fromDate/toDate gửi dạng ISO 19 ký tự — BE nhận LocalDateTime
  if (fromDate)                                          params.fromDate     = fromDate
  if (toDate)                                            params.toDate       = toDate
  if (minTotal != null && minTotal !== '')               params.minTotal     = minTotal
  if (maxTotal != null && maxTotal !== '')               params.maxTotal     = maxTotal

  return await axiosInstance.get(`${ORDER_API}/filter`, { params })
}

export const updateOrderItem = async (itemId, payload) => {
  return await axiosInstance.put(`${ORDER_ITEM_API}/${itemId}`, payload)
}
 

export const deleteOrderItems = async (itemIds) => {
  return await axiosInstance.delete('/order-items', { data: itemIds })
}