import axiosInstance from '../axiosConfig'

const STORE_API = '/stores'

export const registerNewStore = async (payload) => {
  return await axiosInstance.post(`${STORE_API}/register`, payload)
}

export const createStore = async (payload) => {
  return await axiosInstance.post(STORE_API, payload)
}

export const getStoreByOwner = async () => {
  return await axiosInstance.get(`${STORE_API}/current`)
}