import { useState, useMemo } from 'react'
import { useQuery, useQueryClient } from '@tanstack/react-query'
import { useStore } from '../store/StoreContext'
import { getOrders } from '../data/services/orderService'
import { getCustomers } from '../data/services/customerService'

// ─────────────────────────────────────────────────────────────
// CONSTANTS
// ─────────────────────────────────────────────────────────────
const ORDER_STATUSES = [
  {
    value: 'IN_PROGRESS',
    label: 'Đang xử lý',
    bg:   'bg-amber-500/10 text-amber-500 border-amber-500/30',
    dot:  'bg-amber-500',
  },
  {
    value: 'COMPLETED',
    label: 'Hoàn thành',
    bg:   'bg-emerald-500/10 text-emerald-500 border-emerald-500/30',
    dot:  'bg-emerald-500',
  },
  {
    value: 'CANCELLED',
    label: 'Đã huỷ',
    bg:   'bg-rose-500/10 text-rose-400 border-rose-500/30',
    dot:  'bg-rose-500',
  },
  {
    value: 'REFUNDED',
    label: 'Hoàn tiền',
    bg:   'bg-purple-500/10 text-purple-400 border-purple-500/30',
    dot:  'bg-purple-500',
  },
]

const STATUS_MAP = Object.fromEntries(ORDER_STATUSES.map(s => [s.value, s]))

// ─────────────────────────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────────────────────────
const fmtMoney = (n) =>
  n != null ? Number(n).toLocaleString('vi-VN') + 'đ' : '—'

const fmtDate = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  return (
    d.toLocaleDateString('vi-VN') +
    ' · ' +
    d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
  )
}

// ─────────────────────────────────────────────────────────────
// STATUS BADGE
// ─────────────────────────────────────────────────────────────
function StatusBadge({ status }) {
  const s = STATUS_MAP[status] ?? {
    label: status ?? '—',
    bg:    'bg-slate-500/10 text-slate-400 border-slate-500/30',
    dot:   'bg-slate-400',
  }
  return (
    <span className={`inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-[9px] font-bold ${s.bg}`}>
      <span className={`h-1.5 w-1.5 rounded-full ${s.dot}`} />
      {s.label}
    </span>
  )
}

// ─────────────────────────────────────────────────────────────
// SHARED UI
// ─────────────────────────────────────────────────────────────
function Sel({ children, ...props }) {
  return (
    <select
      className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2.5 py-1.5
        text-xs text-[var(--text)] focus:outline-none focus:ring-2
        focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
      {...props}
    >{children}</select>
  )
}

// ─────────────────────────────────────────────────────────────
// ORDER ITEM DETAIL ROW — click để xem thêm note/discount
// ─────────────────────────────────────────────────────────────
function OrderItemRow({ item }) {
  const [expanded, setExpanded] = useState(false)
  const hasExtra = item.note || (item.discountPct && Number(item.discountPct) > 0)

  return (
    <>
      <div
        onClick={() => hasExtra && setExpanded(e => !e)}
        className={`flex items-center justify-between rounded-xl border border-[var(--border)]
          bg-[var(--surface-2)] px-3 py-2 text-xs transition-colors
          ${hasExtra ? 'cursor-pointer hover:border-emerald-500/40 hover:bg-emerald-500/5' : ''}`}
      >
        {/* Tên + variant */}
        <div className="min-w-0 flex-1">
          <div className="flex items-center gap-1.5 flex-wrap">
            <span className="font-semibold text-[var(--text)] truncate">{item.productName}</span>
            {item.variantName && (
              <span className="rounded-full border border-[var(--border)] bg-[var(--surface)]
                px-1.5 py-0.5 text-[9px] font-medium text-[var(--muted)]">
                {item.variantName}
              </span>
            )}
            {hasExtra && (
              <span className="text-[9px] text-[var(--muted)]">{expanded ? '▲' : '▼'}</span>
            )}
          </div>
          <p className="mt-0.5 text-[10px] text-[var(--muted)]">
            x{item.quantity} · {fmtMoney(item.unitPrice)}
          </p>
        </div>
        {/* Line total */}
        <span className="ml-3 shrink-0 font-bold text-emerald-500">
          {fmtMoney(item.lineTotal)}
        </span>
      </div>

      {/* Expanded: note + discount */}
      {expanded && (
        <div className="ml-2 rounded-b-xl border border-t-0 border-[var(--border)]
          bg-[var(--surface)] px-3 py-2 text-[10px] space-y-1">
          {item.note && (
            <p className="text-[var(--muted)]">
              📝 <span className="text-[var(--text)]">{item.note}</span>
            </p>
          )}
          {Number(item.discountPct) > 0 && (
            <p className="text-[var(--muted)]">
              Giảm giá: <span className="font-semibold text-rose-400">
                {item.discountPct}% (−{fmtMoney(item.discountAmt)})
              </span>
            </p>
          )}
        </div>
      )}
    </>
  )
}

// ─────────────────────────────────────────────────────────────
// ORDER DETAIL PANEL
// ─────────────────────────────────────────────────────────────
function OrderDetailPanel({ order, onClose }) {
  const items = order.orderItems ?? []

  // Tính nhanh tổng số lượng
  const totalQty = items.reduce((s, i) => s + (i.quantity ?? 0), 0)

  return (
    <div className="flex h-full flex-col">

      {/* Header */}
      <div className="flex items-center justify-between border-b border-[var(--border)] px-4 py-3 shrink-0">
        <p className="text-sm font-bold text-[var(--text)]">Chi tiết đơn hàng</p>
        <button type="button" onClick={onClose}
          className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
            text-[var(--muted)] hover:border-[var(--text)] transition-all">
          ✕
        </button>
      </div>

      <div className="flex-1 overflow-auto">

        {/* Order code + status */}
        <div className="flex items-center justify-between gap-3 border-b border-[var(--border)] px-4 py-3">
          <div>
            <p className="text-base font-black text-[var(--text)]">
              {order.orderNumber ?? `#${order.orderId}`}
            </p>
            <p className="text-[10px] text-[var(--muted)]">
              Tạo: {fmtDate(order.createdAt)}
            </p>
            {order.completedAt && (
              <p className="text-[10px] text-[var(--muted)]">
                Hoàn thành: {fmtDate(order.completedAt)}
              </p>
            )}
          </div>
          <StatusBadge status={order.status} />
        </div>

        {/* Thông tin order */}
        <div className="border-b border-[var(--border)] px-4 py-3 space-y-2.5">
          {[
            { label: 'Bàn',        value: order.tableId   ? `Bàn #${order.tableId}`   : '—' },
            { label: 'Khách hàng', value: order.customerId ? `KH #${order.customerId}` : 'Vãng lai' },
            { label: 'Nhân viên',  value: order.employeeId ? `NV #${order.employeeId}` : '—' },
            { label: 'Ghi chú',    value: order.note || '—' },
          ].map(row => (
            <div key={row.label} className="flex items-start justify-between gap-2">
              <span className="shrink-0 text-[11px] font-medium text-[var(--muted)]">{row.label}</span>
              <span className="text-right text-[11px] font-semibold text-[var(--text)]">{row.value}</span>
            </div>
          ))}
        </div>

        {/* Tổng tiền */}
        <div className="border-b border-[var(--border)] px-4 py-3 space-y-1.5">
          {[
            { label: 'Tạm tính',   value: fmtMoney(order.subtotal),        muted: true },
            { label: 'Giảm giá',   value: `−${fmtMoney(order.discountAmount)}`, muted: true },
            { label: `VAT (${order.vat ?? 0}%)`, value: fmtMoney(order.taxAmount), muted: true },
          ].map(row => (
            <div key={row.label} className="flex items-center justify-between">
              <span className="text-[11px] text-[var(--muted)]">{row.label}</span>
              <span className="text-[11px] text-[var(--muted)]">{row.value}</span>
            </div>
          ))}
          {/* Grand total */}
          <div className="flex items-center justify-between border-t border-[var(--border)] pt-2 mt-2">
            <span className="text-xs font-bold text-[var(--text)]">Tổng cộng</span>
            <span className="text-sm font-black text-emerald-500">{fmtMoney(order.grandTotal)}</span>
          </div>
        </div>

        {/* Danh sách order items */}
        <div className="px-4 py-3">
          <p className="mb-2 text-[11px] font-bold text-[var(--muted)] uppercase tracking-wide">
            Sản phẩm ({totalQty} món · {items.length} dòng)
          </p>

          {items.length === 0 ? (
            <p className="py-4 text-center text-[11px] text-[var(--muted)]/50">Không có sản phẩm</p>
          ) : (
            <div className="space-y-1.5">
              {items.map(item => (
                <OrderItemRow key={item.orderItemId} item={item} />
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────
// MAIN PAGE
// ─────────────────────────────────────────────────────────────
export function OrdersPage() {
  const { currentStoreId } = useStore()

  // ── Filter state ──────────────────────────────────────────
  const [orderNumber, setOrderNumber] = useState('')
  const [status, setStatus]           = useState('')
  const [customerId, setCustomerId]   = useState('')
  const [fromDate, setFromDate]       = useState('')
  const [toDate, setToDate]           = useState('')
  const [page, setPage]               = useState(1)
  const PAGE_SIZE = 20

  // Build queryParams — loại bỏ field rỗng, giống customerService
  // BE dùng @ModelAttribute → null/empty gây lỗi binding với LocalDateTime/enum
  const queryParams = useMemo(() => {
    const p = { page, size: PAGE_SIZE, storeId: currentStoreId }
    if (orderNumber.trim()) p.orderNumber = orderNumber.trim()
    if (status)             p.status      = status
    if (customerId)         p.customerId  = Number(customerId)
    // Gửi ISO 19 ký tự — BE nhận LocalDateTime
    if (fromDate) p.fromDate = new Date(fromDate).toISOString().slice(0, 19)
    if (toDate)   p.toDate   = new Date(toDate + 'T23:59:59').toISOString().slice(0, 19)
    return p
  }, [page, orderNumber, status, customerId, fromDate, toDate, currentStoreId])

  // ── Fetch orders ──────────────────────────────────────────
  const { data: orderRes, isLoading } = useQuery({
    queryKey: ['orders', queryParams],
    queryFn:  () => getOrders(queryParams),
    enabled:  !!currentStoreId,
    keepPreviousData: true,
  })

  // axios trả về response object → data thực nằm ở response.data (PageDTO)
  const orders        = orderRes?.data?.content       ?? []
  const totalPages    = orderRes?.data?.totalPages    ?? 1
  const totalElements = orderRes?.data?.totalElements ?? 0

  // ── Fetch customers cho dropdown filter ───────────────────
  // Size 100 — đủ nhỏ để load 1 lần, không cần phân trang
  const { data: customerRes } = useQuery({
    queryKey: ['customers-dropdown', currentStoreId],
    queryFn:  () => getCustomers({ storeId: currentStoreId, page: 1, size: 100 }),
    enabled:  !!currentStoreId,
  })
  const customers = customerRes?.data?.content ?? []

  const [selected, setSelected] = useState(null) // order | null

  const resetFilters = () => {
    setOrderNumber(''); setStatus(''); setCustomerId('')
    setFromDate(''); setToDate(''); setPage(1)
  }

  const hasFilter = orderNumber || status || customerId || fromDate || toDate

  return (
    <div className="flex h-[calc(100vh-3.5rem)] overflow-hidden text-[var(--text)]">

      {/* ── LEFT: danh sách ── */}
      <div className={`flex flex-col transition-all duration-300
        ${selected ? 'w-full md:w-[58%] lg:w-[62%]' : 'w-full'}`}>

        {/* Header */}
        <div className="flex flex-wrap items-center justify-between gap-3
          border-b border-[var(--border)] px-4 py-3 shrink-0">
          <div>
            <h1 className="text-base font-bold sm:text-lg text-[var(--text)]">Đơn hàng</h1>
            <p className="text-xs text-[var(--muted)]">
              {totalElements.toLocaleString('vi-VN')} đơn hàng
            </p>
          </div>
        </div>

        {/* Filter bar */}
        <div className="flex flex-wrap gap-2 border-b border-[var(--border)] px-4 py-2.5 shrink-0">

          {/* Tìm mã đơn */}
          <input
            className="min-w-[160px] flex-1 rounded-lg border border-[var(--border)] bg-[var(--input)]
              px-3 py-1.5 text-xs text-[var(--text)] placeholder:text-[var(--muted)]
              focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            placeholder="Tìm theo mã đơn..."
            value={orderNumber}
            onChange={e => { setOrderNumber(e.target.value); setPage(1) }}
          />

          {/* Filter trạng thái */}
          <Sel value={status} onChange={e => { setStatus(e.target.value); setPage(1) }}>
            <option value="">Tất cả trạng thái</option>
            {ORDER_STATUSES.map(s => (
              <option key={s.value} value={s.value}>{s.label}</option>
            ))}
          </Sel>

          {/* Filter khách hàng — lấy từ customerService */}
          <Sel value={customerId} onChange={e => { setCustomerId(e.target.value); setPage(1) }}>
            <option value="">Tất cả khách</option>
            {customers.map(c => (
              <option key={c.customerId} value={c.customerId}>
                {c.fullName}{c.phone ? ` · ${c.phone}` : ''}
              </option>
            ))}
          </Sel>

          {/* Từ ngày */}
          <input
            type="date"
            className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2.5 py-1.5
              text-xs text-[var(--text)] focus:outline-none focus:ring-2
              focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            value={fromDate}
            onChange={e => { setFromDate(e.target.value); setPage(1) }}
          />
          {/* Đến ngày */}
          <input
            type="date"
            className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2.5 py-1.5
              text-xs text-[var(--text)] focus:outline-none focus:ring-2
              focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            value={toDate}
            onChange={e => { setToDate(e.target.value); setPage(1) }}
          />

          {/* Reset filter */}
          {hasFilter && (
            <button type="button" onClick={resetFilters}
              className="rounded-lg border border-[var(--border)] px-2.5 py-1.5 text-[11px]
                text-[var(--muted)] hover:border-rose-400 hover:text-rose-400 transition-all">
              ✕ Xoá lọc
            </button>
          )}
        </div>

        {/* Bảng đơn hàng */}
        <div className="flex-1 overflow-auto">
          {isLoading ? (
            <div className="flex items-center justify-center gap-2 py-20 text-sm text-[var(--muted)]">
              <span className="h-4 w-4 animate-spin rounded-full border-2 border-[var(--border)] border-t-emerald-500" />
              Đang tải...
            </div>
          ) : orders.length === 0 ? (
            <div className="flex flex-col items-center justify-center gap-3 py-20">
              <span className="text-4xl opacity-30">🧾</span>
              <p className="text-sm text-[var(--muted)]">Không tìm thấy đơn hàng nào</p>
            </div>
          ) : (
            <table className="min-w-full text-left text-xs text-[var(--text)]">
              <thead className="sticky top-0 z-10 bg-[var(--surface-2)] text-[var(--muted)]">
                <tr>
                  <th className="px-4 py-2.5 font-semibold">Mã đơn</th>
                  <th className="hidden px-3 py-2.5 font-semibold sm:table-cell">Thời gian</th>
                  <th className="hidden px-3 py-2.5 font-semibold md:table-cell">Bàn / KH</th>
                  <th className="px-3 py-2.5 text-right font-semibold">Tổng tiền</th>
                  <th className="px-3 py-2.5 font-semibold">Trạng thái</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border)]">
                {orders.map(o => (
                  <tr
                    key={o.orderId}
                    onClick={() => setSelected(prev => prev?.orderId === o.orderId ? null : o)}
                    className={`cursor-pointer transition-colors hover:bg-[var(--surface-2)]
                      ${selected?.orderId === o.orderId
                        ? 'bg-emerald-500/5 border-l-2 border-l-emerald-500'
                        : ''}`}
                  >
                    {/* Mã đơn */}
                    <td className="px-4 py-2.5">
                      <p className="font-bold text-[var(--text)]">
                        {o.orderNumber ?? `#${o.orderId}`}
                      </p>
                      <p className="text-[10px] text-[var(--muted)] sm:hidden">
                        {fmtDate(o.createdAt)}
                      </p>
                    </td>
                    {/* Thời gian — ẩn mobile */}
                    <td className="hidden px-3 py-2.5 text-[var(--muted)] sm:table-cell">
                      {fmtDate(o.createdAt)}
                    </td>
                    {/* Bàn / Khách — ẩn mobile */}
                    <td className="hidden px-3 py-2.5 md:table-cell">
                      <p className="text-[var(--text)]">
                        {o.tableId ? `Bàn #${o.tableId}` : '—'}
                      </p>
                      {o.customerId && (
                        <p className="text-[10px] text-[var(--muted)]">KH #{o.customerId}</p>
                      )}
                    </td>
                    {/* Tổng */}
                    <td className="px-3 py-2.5 text-right font-bold text-emerald-500">
                      {fmtMoney(o.grandTotal)}
                    </td>
                    {/* Trạng thái */}
                    <td className="px-3 py-2.5">
                      <StatusBadge status={o.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between border-t border-[var(--border)]
            px-4 py-2.5 text-xs text-[var(--muted)] shrink-0">
            <span>Trang {page} / {totalPages}</span>
            <div className="flex gap-1">
              <button type="button" disabled={page <= 1} onClick={() => setPage(p => p - 1)}
                className="rounded-lg border border-[var(--border)] px-2.5 py-1 disabled:opacity-40
                  hover:border-emerald-500 hover:text-emerald-500 transition-all">
                ‹ Trước
              </button>
              <button type="button" disabled={page >= totalPages} onClick={() => setPage(p => p + 1)}
                className="rounded-lg border border-[var(--border)] px-2.5 py-1 disabled:opacity-40
                  hover:border-emerald-500 hover:text-emerald-500 transition-all">
                Tiếp ›
              </button>
            </div>
          </div>
        )}
      </div>

      {/* ── RIGHT: detail panel — ẩn mobile, hiện từ md ── */}
      {selected && (
        <div className="hidden border-l border-[var(--border)] bg-[var(--surface-solid)]
          md:flex md:w-[42%] lg:w-[38%] flex-col">
          <OrderDetailPanel
            order={selected}
            onClose={() => setSelected(null)}
          />
        </div>
      )}
    </div>
  )
}