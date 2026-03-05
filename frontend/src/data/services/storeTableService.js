import axiosInstance from '../axiosConfig'

const STORE_TABLE_API = '/store-tables'

// POST /store-tables
// payload: { storeZoneId, tableCode, seats }
export const createTable = async (payload) => {
  return await axiosInstance.post(STORE_TABLE_API, payload)
}

// PUT /store-tables/:id
// payload: { storeZoneId, tableCode, seats, status, isActive }
export const updateTable = async (id, payload) => {
  return await axiosInstance.put(`${STORE_TABLE_API}/${id}`, payload)
}