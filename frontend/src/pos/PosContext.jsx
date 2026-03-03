import { createContext, useContext, useMemo, useState } from 'react'
import PropTypes from 'prop-types'
import { MOCK_ORDERS_BY_TABLE, MOCK_PRODUCTS } from '../data/mockPosData'
import { useStore } from '../store/StoreContext'
import { getStoreLayout } from '../data/services/storeZoneApi'
import { getProducts } from '../data/services/productService'
import { useQuery } from '@tanstack/react-query'

const PosContext = createContext(null)

export function PosProvider({ children }) {
  const { currentStoreId } = useStore()

  const { data: storeLayout } = useQuery({
    queryKey: ['storeLayout', currentStoreId],
    queryFn: () => getStoreLayout(currentStoreId),
    enabled: !!currentStoreId,
    staleTime: 1000 * 60 * 5,
    refetchOnWindowFocus: false,
  })

  const { data: productData } = useQuery({
    queryKey: ['posProducts', currentStoreId],
    queryFn: () => getProducts(currentStoreId),
    enabled: !!currentStoreId,
    staleTime: 1000 * 60 * 5,
  })

  // ====== MAP AREAS ======
  const areas = useMemo(() => {
    const zones = storeLayout?.data ?? []
    return zones.map((z) => ({
      id: z.zoneId,
      name: z.zoneName,
    }))
  }, [storeLayout])

  // ====== MAP ALL TABLES ======
  const allTables = useMemo(() => {
    const zones = storeLayout?.data ?? []
    return zones.flatMap((z) =>
      (z.tables ?? []).map((t) => ({
        id: t.tableId,
        name: t.tableCode,
        status: t.status,
        areaId: z.zoneId,
        openedAt: t.openedAt,
        reservedAt: t.reservedAt,
        guests: t.guests,
      })),
    )
  }, [storeLayout])

  // ====== ACTIVE AREA ======
  const [activeAreaId, setActiveAreaId] = useState(null)

  // ====== SELECT TABLE ======
  const [selectedTableId, setSelectedTableId] = useState(null)
  const [orderItems, setOrderItems] = useState([])

  const selectedTable = useMemo(
    () => allTables.find((t) => t.id === selectedTableId) ?? null,
    [selectedTableId, allTables],
  )

  // ====== MOCK ORDER (temporary) ======
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

  // ====== ORDER ACTIONS ======
  const addProductToOrder = (item) => {
    setOrderItems(prev => [
      ...prev,
      {
        id: Date.now(),
        ...item,
        qty: 1,
      }
    ])
  }

  const changeItemQty = (id, delta) => {
    setOrderItems(prev =>
      prev
        .map(i =>
          i.id === id ? { ...i, qty: i.qty + delta } : i
        )
        .filter(i => i.qty > 0)
    )
  }

  const subtotal = orderItems.reduce(
    (sum, item) => sum + item.price * item.qty,
    0,
  )

  // ====== CONTEXT VALUE ======
  const value = {
    areas,
    tables: allTables,        // tất cả bàn đã map từ API
    activeAreaId,
    setActiveAreaId,
    selectedTable,
    selectTable,
    products: productData?.data ?? [],
    orderItems,
    addProductToOrder,
    changeItemQty,
    subtotal,
  }
  console.log(orderItems)

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