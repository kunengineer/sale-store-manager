import axiosInstance from '../axiosConfig'

const PRODUCT_API = '/products'

// POST /products
// payload: { ... ProductCreateRequest fields }
export const createProduct = async (payload) => {
  return await axiosInstance.post(PRODUCT_API, payload)
}

// GET /products/:id
export const getProductById = async (id) => {
  return await axiosInstance.get(`${PRODUCT_API}/${id}`)
}

// PUT /products/:id
// payload: { ... ProductUpdateRequest fields }
export const updateProduct = async (id, payload) => {
  return await axiosInstance.put(`${PRODUCT_API}/${id}`, payload)
}

// GET /products/filter?page=1&size=10&...filterFields
export const getProducts = async (page = 1, size = 10, filter = {}) => {
  return await axiosInstance.get(`${PRODUCT_API}/filter`, {
    params: { page, size, ...filter },
  })
}

// GET /products/pos?storeId=...
export const getProductsForPos = async (storeId) => {
  return await axiosInstance.get(`${PRODUCT_API}/pos`, {
    params: { storeId },
  })
}