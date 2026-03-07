import axiosInstance from '../axiosConfig'

const CATEGORY_API = '/categories'

// GET /categories/pos?storeId=...
export const getCategories = async (storeId, page = 1, size = 20) => {
  return await axiosInstance.get(`${CATEGORY_API}/stores/${storeId}/categories`, {
    params: {
      page,
      size,
    },
  });
};