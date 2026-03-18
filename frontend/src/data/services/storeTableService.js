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


export const moveTableApi = async (payload) => {
  return axiosInstance.put(`${STORE_TABLE_API}/move`, payload)
}

export const getTablesApi = async () => {
  return axiosInstance.get(`/store-tables/filter?page=1&size=100`);
};