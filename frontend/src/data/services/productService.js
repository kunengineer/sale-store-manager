import axiosInstance from '../axiosConfig'

const PRODUCT_API = '/products'

// GET /products/pos?storeId=...
export const getProducts = async (storeId) => {
  return await axiosInstance.get(`${PRODUCT_API}/pos`, {
    params: { storeId },
  })
}