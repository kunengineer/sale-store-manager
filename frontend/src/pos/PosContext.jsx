import { createContext, useContext, useMemo, useState } from 'react'
import PropTypes from 'prop-types'
import { MOCK_ORDERS_BY_TABLE, MOCK_PRODUCTS } from '../data/mockPosData'
import { useStore } from '../store/StoreContext'
import { getStoreLayout } from '../data/services/storeZoneApi'
import { getProductsForPos } from '../data/services/productService'
import { getOpenOrderByTable } from '../data/services/orderService'
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
    queryFn: () => getProductsForPos(currentStoreId),
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
  const [currentOrderId, setCurrentOrderId] = useState(null)
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


  const selectTable = async (tableId) => {
    setSelectedTableId(tableId)
    setOrderItems([])
    setCurrentOrderId(null)

    try {
      const order = await getOpenOrderByTable(tableId)
      console.log('Lấy order mở của bàn', tableId, ':', order)
      if (order) {
        setCurrentOrderId(order.data.orderId)
        setOrderItems(
          (order.data.orderItems ?? []).map((item) => ({
            id:          item.orderItemId,  // key FE
            orderItemId: item.orderItemId,  // đánh dấu đã có trên BE
            productName: item.productName,
            base:        item.base,
            toppings:    item.toppings ?? [],
            note:        item.note ?? '',
            qty:         item.quantity,
            price:       item.unitPrice,
          }))
        )
      }
    } catch (err) {
      if (err?.status !== 404) console.error('Lỗi load order:', err)
      // 404 = bàn trống → bình thường
    }
  }

  // ====== ORDER ACTIONS ======
  const addProductToOrder = (item) => {
    setOrderItems((prev) => [
      ...prev,
      { id: Date.now(), ...item, qty: 1 },
      // id = Date.now(), KHÔNG có orderItemId → FE biết đây là item mới
    ])
  }

  const changeItemQty = (id, delta) => {
    setOrderItems((prev) =>
      prev.map((i) => i.id === id ? { ...i, qty: i.qty + delta } : i)
          .filter((i) => i.qty > 0)
    )
  }

  // Xóa hẳn 1 item — BE sẽ được gọi deleteOrderItem khi Lưu tạm
  const removeItem = (id) => {
    setOrderItems((prev) => prev.filter((i) => i.id !== id))
  }

  const updateItemNote = (id, note) => {
    setOrderItems((prev) =>
      prev.map((i) => i.id === id ? { ...i, note } : i)
    )
  }

  const subtotal = useMemo(
    () => orderItems.reduce((sum, i) => sum + i.price * i.qty, 0),
    [orderItems],
  )

  /* const subtotal = orderItems.reduce(
    (sum, item) => sum + item.price * item.qty,
    0,
  ) */

  // ====== CONTEXT VALUE ======
  const value = {
    areas, tables: allTables, activeAreaId, setActiveAreaId,
    selectedTable, selectTable,
    products: productData?.data ?? [],
    orderItems, addProductToOrder, changeItemQty, subtotal,
    currentOrderId, setCurrentOrderId, updateItemNote, removeItem
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