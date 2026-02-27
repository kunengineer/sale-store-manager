export const MOCK_STORES = [
  { id: 'store-1', name: 'Chi nhánh Nguyễn Trãi' },
  { id: 'store-2', name: 'Chi nhánh Quận 1' },
  { id: 'store-3', name: 'Chi nhánh Thủ Đức' },
]

export const MOCK_AREAS = [
  { id: 'area-1', storeId: 'store-1', name: 'Tầng 1' },
  { id: 'area-2', storeId: 'store-1', name: 'Tầng 2' },
  { id: 'area-3', storeId: 'store-1', name: 'Sân vườn' },
]

export const MOCK_TABLES = [
  {
    id: 'table-a1',
    areaId: 'area-1',
    name: 'A1',
    status: 'using',
    guests: 2,
    openedAt: '2026-02-27T10:05:00+07:00',
  },
  { id: 'table-a2', areaId: 'area-1', name: 'A2', status: 'empty', guests: 0 },
  {
    id: 'table-a3',
    areaId: 'area-1',
    name: 'A3',
    status: 'reserved',
    guests: 0,
    reservedAt: '2026-02-27T10:30:00+07:00',
  },
  {
    id: 'table-b1',
    areaId: 'area-2',
    name: 'B1',
    status: 'using',
    guests: 4,
    openedAt: '2026-02-27T09:45:00+07:00',
  },
]

export const MOCK_PRODUCTS = [
  {
    id: 'p1',
    name: 'Cà phê sữa đá',
    category: 'Cafe',
    tag: 'Best seller',
    price: 32000,
  },
  {
    id: 'p2',
    name: 'Bạc xỉu',
    category: 'Cafe',
    price: 29000,
  },
  {
    id: 'p3',
    name: 'Trà đào cam sả',
    category: 'Trà',
    tag: 'Hot',
    price: 39000,
  },
  {
    id: 'p4',
    name: 'Mochi trà xanh',
    category: 'Bánh',
    price: 22000,
  },
]

// Đơn hàng mẫu theo bàn để load khi chọn bàn
export const MOCK_ORDERS_BY_TABLE = {
  'table-a1': [
    { productId: 'p1', qty: 2, note: 'Ít đá, thêm sữa' },
    { productId: 'p3', qty: 1 },
  ],
  'table-b1': [{ productId: 'p2', qty: 3 }],
}

