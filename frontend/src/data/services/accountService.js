import axiosInstance from '../axiosConfig'

const ACCOUNT_API = '/accounts'

// POST /accounts/login
export const signIn = async (username, password) => {
  return await axiosInstance.post(`${ACCOUNT_API}/login`, { username, password })
}

// POST /accounts — tạo tài khoản mới
// payload: { fullName, username, email, password, role }
// trả về: APIResponse<AccountResponse> → interceptor unwrap còn { accountId, fullName, userName, email, createdAt, role }
export const createAccount = async (payload) => {
  return await axiosInstance.post(ACCOUNT_API, payload)
}

// GET /accounts/:id
export const getAccountById = async (id) => {
  return await axiosInstance.get(`${ACCOUNT_API}/${id}`)
}

// PUT /accounts/:id
export const updateAccount = async (id, payload) => {
  return await axiosInstance.put(`${ACCOUNT_API}/${id}`, payload)
}

// GET /accounts
export const getAccounts = async () => {
  return await axiosInstance.get(ACCOUNT_API)
}