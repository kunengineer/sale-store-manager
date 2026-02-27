import { createContext, useContext, useMemo, useState } from 'react'
import PropTypes from 'prop-types'
import {
  MOCK_AREAS,
  MOCK_ORDERS_BY_TABLE,
  MOCK_PRODUCTS,
  MOCK_TABLES,
} from '../data/mockPosData'
import { useStore } from '../store/StoreContext'

const PosContext = createContext(null)

export function PosProvider({ children }) {
  const { currentStoreId } = useStore()

  const areasForStore = useMemo(
    () => MOCK_AREAS.filter((a) => a.storeId === currentStoreId),
    [currentStoreId],
  )

  const [activeAreaId, setActiveAreaId] = useState(
    areasForStore[0]?.id ?? MOCK_AREAS[0]?.id,
  )
  const [selectedTableId, setSelectedTableId] = useState(null)
  const [orderItems, setOrderItems] = useState([])

  const tablesForArea = useMemo(
    () => MOCK_TABLES.filter((t) => t.areaId === activeAreaId),
    [activeAreaId],
  )

  const selectedTable = useMemo(
    () => MOCK_TABLES.find((t) => t.id === selectedTableId) ?? null,
    [selectedTableId],
  )

  const hydrateOrderFromMock = (tableId) => {
    const mock = MOCK_ORDERS_BY_TABLE[tableId] ?? []
    const hydrated = mock.map((item) => {
      const product = MOCK_PRODUCTS.find((p) => p.id === item.productId)
      return {
        ...item,
        name: product?.name ?? 'Món',
        price: product?.price ?? 0,
      }
    })
    setOrderItems(hydrated)
  }

  const selectTable = (tableId) => {
    setSelectedTableId(tableId)
    hydrateOrderFromMock(tableId)
  }

  const addProductToOrder = (productId) => {
    const product = MOCK_PRODUCTS.find((p) => p.id === productId)
    if (!product) return
    setOrderItems((prev) => {
      const existing = prev.find((i) => i.productId === productId)
      if (existing) {
        return prev.map((i) =>
          i.productId === productId ? { ...i, qty: i.qty + 1 } : i,
        )
      }
      return [
        ...prev,
        {
          productId,
          name: product.name,
          price: product.price,
          qty: 1,
        },
      ]
    })
  }

  const changeItemQty = (productId, delta) => {
    setOrderItems((prev) =>
      prev
        .map((i) =>
          i.productId === productId ? { ...i, qty: i.qty + delta } : i,
        )
        .filter((i) => i.qty > 0),
    )
  }

  const subtotal = orderItems.reduce((sum, item) => sum + item.price * item.qty, 0)

  const value = {
    areas: areasForStore,
    tables: tablesForArea,
    allTables: MOCK_TABLES,
    activeAreaId,
    setActiveAreaId,
    selectedTable,
    selectTable,
    products: MOCK_PRODUCTS,
    orderItems,
    addProductToOrder,
    changeItemQty,
    subtotal,
  }

  return <PosContext.Provider value={value}>{children}</PosContext.Provider>
}

PosProvider.propTypes = {
  children: PropTypes.node,
}

export function usePos() {
  const ctx = useContext(PosContext)
  if (!ctx) throw new Error('usePos must be used within PosProvider')
  return ctx
}

