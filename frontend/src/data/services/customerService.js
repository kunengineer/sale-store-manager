import axiosInstance from '../axiosConfig'

const CUSTOMER_API = '/customers'

// POS SEARCH CUSTOMER
export const searchCustomersPOS = async ({ keyword, storeId }) => {
  const params = { storeId }

  if (keyword?.trim()) params.keyword = keyword.trim()

  return await axiosInstance.get(`${CUSTOMER_API}/pos-search`, { params })
}

// GET /customers/filter?fullName=...&storeId=...&page=1&size=20
// Lưu ý: @ModelAttribute + @Getter → chỉ gửi field có giá trị, bỏ qua null/undefined
export const getCustomers = async ({ page = 1, size = 20, fullName, customerCode, storeId } = {}) => {
  const params = { page, size, storeId }
  // Chỉ append nếu có giá trị — tránh Spring binding null từ empty string
  if (fullName?.trim())     params.fullName     = fullName.trim()
  if (customerCode?.trim()) params.customerCode = customerCode.trim()
  return await axiosInstance.get(`${CUSTOMER_API}/filter`, { params })
}

// GET /customers/:id
export const getCustomerById = async (id) => {
  return await axiosInstance.get(`${CUSTOMER_API}/${id}`)
}

// POST /customers
// payload: { fullName, phone, gender, note, storeId }
export const createCustomer = async (payload) => {
  return await axiosInstance.post(CUSTOMER_API, payload)
}

// PUT /customers/:id
// payload: { fullName, phone, gender, note, storeId }
export const updateCustomer = async (id, payload) => {
  return await axiosInstance.put(`${CUSTOMER_API}/${id}`, payload)
}

// DELETE /customers/:id  (mình tự kiểm tra ràng buộc)
export const deleteCustomer = async (id) => {
  return await axiosInstance.delete(`${CUSTOMER_API}/${id}`)
}