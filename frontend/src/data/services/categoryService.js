import axiosInstance from '../axiosConfig'

const CATEGORY_API = '/categories'

// POST /categories
// payload: { ... CategoryCreateRequest fields }
export const createCategory = async (payload) => {
  return await axiosInstance.post(CATEGORY_API, payload)
}

// GET /categories/:id
export const getCategoryById = async (id) => {
  return await axiosInstance.get(`${CATEGORY_API}/${id}`)
}

// PUT /categories/:id
// payload: { ... CategoryUpdateRequest fields }
export const updateCategory = async (id, payload) => {
  return await axiosInstance.put(`${CATEGORY_API}/${id}`, payload)
}

// GET /categories/stores/:storeId?page=1&size=10&parentId=...&categoryName=...&isActive=...
export const getCategories = async (storeId, page = 1, size = 10, filter = {}) => {
  return await axiosInstance.get(`${CATEGORY_API}/stores/${storeId}`, {
    params: { page, size, ...filter },
  })
}