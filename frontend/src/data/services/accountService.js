import axiosInstance from '../axiosConfig'

const ACCOUNT_API = '/accounts'

export const signIn = async (username, password) => {
  return await axiosInstance.post(`${ACCOUNT_API}/login`, { username, password })
}

// GET /accounts
export const getAccounts = async () => {
  return await axiosInstance.get('/accounts')
}

// GET /accounts/:id  (tùy BE có endpoint này không)
export const getAccountById = async (id) => {
  return await axiosInstance.get(`/accounts/${id}`)
}