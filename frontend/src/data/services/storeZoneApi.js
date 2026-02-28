import axiosInstance from '../axiosConfig'

const STORE_ZONE_API = '/store-zones'

// GET /store-zones/layout?storeId=1
export const getStoreLayout = async (storeId) => {
  return await axiosInstance.get(`${STORE_ZONE_API}/layout`, {
    params: { storeId },
  })
}