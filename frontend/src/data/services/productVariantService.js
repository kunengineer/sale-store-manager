import axiosInstance from '../axiosConfig'

const PRODUCT_VARIANT_API = '/product-variants'

// POST /product-variants
// payload: { ... ProductVariantCreateRequest fields }
export const createProductVariant = async (payload) => {
  return await axiosInstance.post(PRODUCT_VARIANT_API, payload)
}

// GET /product-variants/:id
export const getProductVariantById = async (id) => {
  return await axiosInstance.get(`${PRODUCT_VARIANT_API}/${id}`)
}

// PUT /product-variants/:id
// payload: { ... ProductVariantUpdateRequest fields }
export const updateProductVariant = async (id, payload) => {
  return await axiosInstance.put(`${PRODUCT_VARIANT_API}/${id}`, payload)
}

// GET /product-variants/all/:productId
export const getProductVariantsByProductId = async (productId) => {
  return await axiosInstance.get(`${PRODUCT_VARIANT_API}/all/${productId}`)
}