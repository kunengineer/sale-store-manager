import axiosInstance from '../axiosConfig'

const STORE_API = '/stores'

export const createStore = async (payload) => {
  return await axiosInstance.post(STORE_API, payload)
}