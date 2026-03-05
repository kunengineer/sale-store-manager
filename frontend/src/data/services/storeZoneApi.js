import axiosInstance from '../axiosConfig'

const STORE_ZONE_API = '/store-zones'

// GET /store-zones/layout?storeId=1
export const getStoreLayout = async (storeId) => {
  return await axiosInstance.get(`${STORE_ZONE_API}/layout`, {
    params: { storeId },
  })
}

// POST /store-zones
// payload: { storeId, zoneName, zoneType, capacity, isActive }
export const createZone = async (payload) => {
  return await axiosInstance.post(STORE_ZONE_API, payload)
}

// PUT /store-zones/:id
// payload: { storeId, zoneName, zoneType, capacity, isActive }
export const updateZone = async (id, payload) => {
  return await axiosInstance.put(`${STORE_ZONE_API}/${id}`, payload)
}