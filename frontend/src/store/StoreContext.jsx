import { createContext, useContext, useState } from 'react'
import PropTypes from 'prop-types'
import { MOCK_STORES } from '../data/mockPosData'

const StoreContext = createContext(null)

export function StoreProvider({ children }) {
  const [currentStoreId, setCurrentStoreId] = useState(MOCK_STORES[0].id)
  // Cài đặt giao diện/hệ thống tạm thời để sau này nối API
  const [settings, setSettings] = useState({
    autoDarkModeOnPos: true,
    hideSidebarInPos: false,
  })
  const [theme, setTheme] = useState('dark')

  const toggleTheme = () =>
    setTheme((prev) => (prev === 'dark' ? 'light' : 'dark'))

  const value = {
    stores: MOCK_STORES,
    currentStoreId,
    setCurrentStoreId,
    currentStore: MOCK_STORES.find((s) => s.id === currentStoreId),
    settings,
    setSettings,
    theme,
    toggleTheme,
  }

  return <StoreContext.Provider value={value}>{children}</StoreContext.Provider>
}

StoreProvider.propTypes = {
  children: PropTypes.node,
}

export function useStore() {
  const ctx = useContext(StoreContext)
  if (!ctx) {
    throw new Error('useStore must be used within StoreProvider')
  }
  return ctx
}

