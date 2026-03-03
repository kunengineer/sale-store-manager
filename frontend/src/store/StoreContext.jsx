import { createContext, useContext, useState, useEffect } from 'react'
import PropTypes from 'prop-types'
import { useNavigate } from 'react-router-dom'
import { getStoreByOwner } from '../data/services/storeService'

const StoreContext = createContext(null)

export function StoreProvider({ children }) {
  const navigate = useNavigate()

  const [stores, setStores] = useState([])
  const [currentStoreId, setCurrentStoreId] = useState(null)
  const [loadingStore, setLoadingStore] = useState(true)

  const [settings, setSettings] = useState({
    autoDarkModeOnPos: true,
    hideSidebarInPos: false,
  })
  const [theme, setTheme] = useState('light')
  const toggleTheme = () => setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'))

  useEffect(() => {
    const fetchStores = async () => {
      try {
        // interceptor đã unwrap APIResponse → res = data (array stores)
        const res = await getStoreByOwner()
        
        console.log('Fetched stores:', res)
        if (res.data.length === 0) {
          // Không có store → redirect sang bước 2 đăng ký
          // accountId lấy từ JWT ở BE, không cần truyền
          // navigate('/register', { state: { step: 2 } })

          setStores([])
          setCurrentStoreId(null)
          return
          return
        }

        setStores(res.data)
        setCurrentStoreId(res.data[0].storeId) // mặc định chọn store đầu tiên
        console.log('Current store ID set to:', res.data[0].storeId)
        console.log('Current store ID set to:', currentStoreId)
      } catch (err) {
        console.error('Failed to fetch stores:', err)
        // Nếu 401/403 → về login
        if (err.status === 401 || err.status === 403) {
          navigate('/login')
        }
      } finally {
        setLoadingStore(false)
      }
    }

    fetchStores()
  }, [navigate])

  const value = {
    stores: stores.map((s) => ({
      id: s.storeId,
      name: s.storeName,
    })),
    currentStoreId: currentStoreId,
    setCurrentStoreId,
    currentStore: stores.find((s) => s.storeId === currentStoreId) ?? null,
    loadingStore,
    settings,
    setSettings,
    theme,
    toggleTheme,
  }

  // Chờ fetch xong mới render children tránh flash UI
  if (loadingStore) {
    return (
      <div className="flex min-h-screen items-center justify-center bg-slate-950">
        <div className="flex items-center gap-3 text-slate-400 text-xs">
          <span className="h-4 w-4 rounded-full border-2 border-slate-600 border-t-emerald-500 animate-spin" />
          Đang tải dữ liệu cửa hàng...
        </div>
      </div>
    )
  }

  return <StoreContext.Provider value={value}>{children}</StoreContext.Provider>
}

StoreProvider.propTypes = {
  children: PropTypes.node,
}

export function useStore() {
  const ctx = useContext(StoreContext)
  if (!ctx) throw new Error('useStore must be used within StoreProvider')
  return ctx
}