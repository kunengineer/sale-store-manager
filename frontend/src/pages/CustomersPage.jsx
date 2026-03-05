import { useState, useMemo } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useStore } from '../store/StoreContext'
import {
  getCustomers,
  createCustomer,
  updateCustomer,
  deleteCustomer,
} from '../data/services/customerService'

// ── CONSTANTS ─────────────────────────────────────────────────
const GENDERS = [
  { value: 'MALE',   label: 'Nam' },
  { value: 'FEMALE', label: 'Nữ' },
  { value: 'OTHER',  label: 'Khác' },
]

const GENDER_LABEL = { MALE: 'Nam', FEMALE: 'Nữ', OTHER: 'Khác' }

const EMPTY_FORM = { fullName: '', phone: '', gender: 'MALE', note: '' }

// ── HELPERS ───────────────────────────────────────────────────
const fmtMoney = (n) =>
  n != null ? Number(n).toLocaleString('vi-VN') + 'đ' : '—'

const fmtDate = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  return d.toLocaleDateString('vi-VN') + ' · ' + d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
}

const getInitials = (name = '') =>
  name.trim().split(' ').slice(-2).map(w => w[0]).join('').toUpperCase()

const getTierFromPoints = (pts = 0) => {
  if (pts >= 5000) return { label: 'Platinum', bg: 'bg-cyan-500/15 text-cyan-400 border-cyan-500/30' }
  if (pts >= 1000) return { label: 'Gold',     bg: 'bg-amber-500/15 text-amber-400 border-amber-500/30' }
  return               { label: 'Silver',    bg: 'bg-slate-500/15 text-slate-400 border-slate-500/30' }
}

// ── SHARED UI ─────────────────────────────────────────────────
function Field({ label, error, children }) {
  return (
    <div>
      <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">{label}</label>
      {children}
      {error && <p className="mt-0.5 text-[10px] font-semibold text-rose-500">{error}</p>}
    </div>
  )
}

function Inp({ error, ...props }) {
  return (
    <input
      className={`w-full rounded-lg border px-2.5 py-1.5 text-xs text-[var(--text)] bg-[var(--input)]
        focus:outline-none focus:ring-2 transition-all
        ${error ? 'border-rose-400 focus:ring-rose-400/30'
                : 'border-[var(--border)] focus:ring-emerald-500/30 focus:border-emerald-500'}`}
      {...props}
    />
  )
}

function Sel({ children, ...props }) {
  return (
    <select
      className="w-full rounded-lg border border-[var(--border)] px-2.5 py-1.5 text-xs
        text-[var(--text)] bg-[var(--input)] focus:outline-none focus:ring-2
        focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
      {...props}
    >{children}</select>
  )
}

// ── CUSTOMER FORM MODAL ───────────────────────────────────────
function CustomerFormModal({ mode, initial, onClose, onSuccess, storeId }) {
  const [form, setForm]     = useState(initial ?? EMPTY_FORM)
  const [errors, setErrors] = useState({})

  const mutation = useMutation({
    mutationFn: (data) =>
      mode === 'create'
        ? createCustomer({ ...data, storeId })
        : updateCustomer(initial.customerId, { ...data, storeId }),
    onSuccess: () => { onSuccess(); onClose() },
  })

  const validate = () => {
    const e = {}
    if (!form.fullName.trim()) e.fullName = 'Vui lòng nhập họ tên'
    if (form.phone && !/^(0|\+84)[0-9]{9,10}$/.test(form.phone))
      e.phone = 'Số điện thoại không hợp lệ'
    return e
  }

  const handleSubmit = () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }
    mutation.mutate(form)
  }

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-4 backdrop-blur-sm">
      <div className="w-full max-w-md rounded-2xl border border-[var(--border)] bg-[var(--surface-solid)] p-5 shadow-2xl">
        <div className="mb-4 flex items-center justify-between">
          <h3 className="text-sm font-bold text-[var(--text)]">
            {mode === 'create' ? '+ Thêm khách hàng' : `Cập nhật · ${initial?.fullName}`}
          </h3>
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
              text-[var(--muted)] hover:border-rose-400 hover:text-rose-400 transition-all">
            ✕
          </button>
        </div>

        <div className="space-y-3">
          <Field label="Họ tên *" error={errors.fullName}>
            <Inp placeholder="Nguyễn Văn A" value={form.fullName}
              onChange={e => setForm(f => ({ ...f, fullName: e.target.value }))}
              error={errors.fullName} />
          </Field>
          <Field label="Số điện thoại" error={errors.phone}>
            <Inp placeholder="0912 345 678" value={form.phone}
              onChange={e => setForm(f => ({ ...f, phone: e.target.value }))}
              error={errors.phone} />
          </Field>
          <Field label="Giới tính">
            <Sel value={form.gender} onChange={e => setForm(f => ({ ...f, gender: e.target.value }))}>
              {GENDERS.map(g => <option key={g.value} value={g.value}>{g.label}</option>)}
            </Sel>
          </Field>
          <Field label="Ghi chú">
            <textarea
              rows={2}
              placeholder="Khách VIP, dị ứng thực phẩm..."
              value={form.note}
              onChange={e => setForm(f => ({ ...f, note: e.target.value }))}
              className="w-full resize-none rounded-lg border border-[var(--border)] bg-[var(--input)]
                px-2.5 py-1.5 text-xs text-[var(--text)] focus:outline-none
                focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            />
          </Field>
        </div>

        {mutation.isError && (
          <p className="mt-3 rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] font-medium text-rose-500">
            ⚠ {mutation.error?.message ?? 'Có lỗi xảy ra'}
          </p>
        )}

        <div className="mt-5 flex justify-end gap-2">
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-4 py-1.5 text-xs
              text-[var(--text)] hover:bg-[var(--surface-2)] transition-colors">
            Hủy
          </button>
          <button type="button" onClick={handleSubmit} disabled={mutation.isPending}
            className="flex items-center gap-1.5 rounded-lg bg-emerald-500 px-5 py-1.5
              text-xs font-bold text-white disabled:opacity-60 hover:bg-emerald-600 transition-colors">
            {mutation.isPending && (
              <span className="h-3 w-3 rounded-full border-2 border-white border-t-transparent animate-spin" />
            )}
            {mutation.isPending ? 'Đang lưu...' : 'Lưu'}
          </button>
        </div>
      </div>
    </div>
  )
}

// ── CUSTOMER DETAIL PANEL ─────────────────────────────────────
function CustomerDetailPanel({ customer, onEdit, onDelete, onClose }) {
  const tier = getTierFromPoints(customer.loyaltyPoints)

  return (
    <div className="flex h-full flex-col">
      {/* Header */}
      <div className="flex items-center justify-between border-b border-[var(--border)] px-4 py-3">
        <p className="text-sm font-bold text-[var(--text)]">Chi tiết khách hàng</p>
        <button type="button" onClick={onClose}
          className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
            text-[var(--muted)] hover:border-[var(--text)] transition-all">
          ✕
        </button>
      </div>

      {/* Avatar + name */}
      <div className="flex flex-col items-center gap-2 border-b border-[var(--border)] px-4 py-5">
        <div className="flex h-14 w-14 items-center justify-center rounded-2xl
          bg-emerald-500/15 text-xl font-black text-emerald-500">
          {getInitials(customer.fullName)}
        </div>
        <div className="text-center">
          <p className="text-sm font-bold text-[var(--text)]">{customer.fullName}</p>
          <p className="text-[10px] text-[var(--muted)]">{customer.customerCode}</p>
        </div>
        <span className={`rounded-full border px-2.5 py-0.5 text-[10px] font-bold ${tier.bg}`}>
          {tier.label}
        </span>
      </div>

      {/* Info rows */}
      <div className="flex-1 overflow-auto px-4 py-3 space-y-3">
        {[
          { label: 'Số điện thoại', value: customer.phone || '—' },
          { label: 'Giới tính',     value: GENDER_LABEL[customer.gender] ?? '—' },
          { label: 'Điểm tích lũy', value: (customer.loyaltyPoints ?? 0).toLocaleString('vi-VN') + ' điểm' },
          { label: 'Tổng chi tiêu', value: fmtMoney(customer.totalSpent) },
          { label: 'Ngày tạo',      value: fmtDate(customer.createdAt) },
          { label: 'Ghi chú',       value: customer.note || '—' },
        ].map(row => (
          <div key={row.label} className="flex items-start justify-between gap-3">
            <span className="shrink-0 text-[11px] font-medium text-[var(--muted)]">{row.label}</span>
            <span className="text-right text-[11px] font-semibold text-[var(--text)]">{row.value}</span>
          </div>
        ))}
      </div>

      {/* Actions */}
      <div className="flex gap-2 border-t border-[var(--border)] px-4 py-3">
        <button type="button" onClick={onEdit}
          className="flex-1 rounded-lg bg-emerald-500 py-2 text-xs font-bold
            text-white hover:bg-emerald-600 transition-colors">
          Chỉnh sửa
        </button>
        <button type="button" onClick={onDelete}
          className="rounded-lg border border-rose-500/40 bg-rose-500/10 px-3 py-2
            text-xs font-bold text-rose-400 hover:bg-rose-500/20 transition-colors">
          Xóa
        </button>
      </div>
    </div>
  )
}

// ── MAIN PAGE ─────────────────────────────────────────────────
export function CustomersPage() {
  const { currentStoreId } = useStore()
  const queryClient = useQueryClient()

  // ── Filter state ─────────────────────────────────────────
  const [search, setSearch]   = useState('')
  const [page, setPage]       = useState(1)
  const PAGE_SIZE = 20

  // Build query params — chỉ gửi field có giá trị (BE dùng @ModelAttribute + @Getter)
  const queryParams = useMemo(() => ({
    page,
    size: PAGE_SIZE,
    storeId: currentStoreId,
    ...(search.trim() ? {
      // Nếu search giống số điện thoại → gửi vào fullName, BE filter OR
      fullName: search.trim(),
    } : {}),
  }), [page, search, currentStoreId])

  const { data, isLoading } = useQuery({
    queryKey: ['customers', queryParams],
    queryFn: () => getCustomers(queryParams),
    enabled: !!currentStoreId,
    keepPreviousData: true,
  })
  
  console.log('Fetched customers:', data)

  const customers   = data?.data.content ?? []
  const totalPages  = data?.data.totalPages ?? 1
  const totalElements = data?.data.totalElements ?? 0

  const invalidate = () => queryClient.invalidateQueries({ queryKey: ['customers'] })

  // ── Modal / panel state ───────────────────────────────────
  const [formModal, setFormModal]       = useState(null)   // null | 'create' | customer obj (edit)
  const [selectedCustomer, setSelected] = useState(null)   // customer obj | null (detail panel)
  const [deleteTarget, setDeleteTarget] = useState(null)   // customer obj | null

  // ── Delete mutation ───────────────────────────────────────
  const deleteMutation = useMutation({
    mutationFn: (id) => deleteCustomer(id),
    onSuccess: () => {
      invalidate()
      setDeleteTarget(null)
      if (selectedCustomer?.customerId === deleteTarget?.customerId) setSelected(null)
    },
  })

  // ── Tier badge helper ─────────────────────────────────────
  const TierBadge = ({ pts }) => {
    const t = getTierFromPoints(pts)
    return (
      <span className={`rounded-full border px-1.5 py-0.5 text-[9px] font-bold ${t.bg}`}>
        {t.label}
      </span>
    )
  }

  return (
    <div className="flex h-[calc(100vh-3.5rem)] overflow-hidden text-[var(--text)]">

      {/* ── LEFT: list ── */}
      <div className={`flex flex-col transition-all duration-300 ${selectedCustomer ? 'w-full md:w-[60%] lg:w-[65%]' : 'w-full'}`}>

        {/* Header */}
        <div className="flex flex-wrap items-center justify-between gap-3 border-b border-[var(--border)] px-4 py-3">
          <div>
            <h1 className="text-base font-bold sm:text-lg text-[var(--text)]">Khách hàng</h1>
            <p className="text-xs text-[var(--muted)]">
              {totalElements.toLocaleString('vi-VN')} khách hàng
            </p>
          </div>
          <button type="button" onClick={() => setFormModal('create')}
            className="rounded-xl bg-emerald-500 px-4 py-2 text-xs font-bold text-white
              hover:bg-emerald-600 active:scale-95 transition-all shadow-sm shadow-emerald-500/30">
            + Thêm khách hàng
          </button>
        </div>

        {/* Search */}
        <div className="flex gap-2 border-b border-[var(--border)] px-4 py-2.5">
          <input
            className="flex-1 rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-1.5
              text-xs text-[var(--text)] placeholder:text-[var(--muted)]
              focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            placeholder="Tìm theo tên, mã khách hàng..."
            value={search}
            onChange={e => { setSearch(e.target.value); setPage(1) }}
          />
        </div>

        {/* Table */}
        <div className="flex-1 overflow-auto">
          {isLoading ? (
            <div className="flex items-center justify-center gap-2 py-20 text-sm text-[var(--muted)]">
              <span className="h-4 w-4 animate-spin rounded-full border-2 border-[var(--border)] border-t-emerald-500" />
              Đang tải...
            </div>
          ) : customers.length === 0 ? (
            <div className="flex flex-col items-center justify-center gap-3 py-20">
              <span className="text-4xl opacity-30">👤</span>
              <p className="text-sm text-[var(--muted)]">Không tìm thấy khách hàng nào</p>
            </div>
          ) : (
            <table className="min-w-full text-left text-xs text-[var(--text)]">
              <thead className="sticky top-0 z-10 bg-[var(--surface-2)] text-[var(--muted)]">
                <tr>
                  <th className="px-4 py-2.5 font-semibold">Khách hàng</th>
                  <th className="px-3 py-2.5 font-semibold">SĐT</th>
                  <th className="hidden px-3 py-2.5 font-semibold sm:table-cell">Điểm</th>
                  <th className="hidden px-3 py-2.5 text-right font-semibold md:table-cell">Chi tiêu</th>
                  <th className="px-3 py-2.5 font-semibold">Hạng</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border)]">
                {customers.map(c => (
                  <tr
                    key={c.customerId}
                    onClick={() => setSelected(prev => prev?.customerId === c.customerId ? null : c)}
                    className={`cursor-pointer transition-colors hover:bg-[var(--surface-2)]
                      ${selectedCustomer?.customerId === c.customerId ? 'bg-emerald-500/5 border-l-2 border-l-emerald-500' : ''}`}
                  >
                    <td className="px-4 py-2.5">
                      <div className="flex items-center gap-2.5">
                        <div className="flex h-7 w-7 shrink-0 items-center justify-center
                          rounded-lg bg-emerald-500/10 text-[11px] font-black text-emerald-500">
                          {getInitials(c.fullName)}
                        </div>
                        <div>
                          <p className="font-semibold text-[var(--text)]">{c.fullName}</p>
                          <p className="text-[10px] text-[var(--muted)]">{c.customerCode}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-3 py-2.5 text-[var(--muted)]">{c.phone || '—'}</td>
                    <td className="hidden px-3 py-2.5 sm:table-cell">
                      <span className="font-semibold text-emerald-500">
                        {(c.loyaltyPoints ?? 0).toLocaleString('vi-VN')}
                      </span>
                    </td>
                    <td className="hidden px-3 py-2.5 text-right font-semibold text-emerald-400 md:table-cell">
                      {fmtMoney(c.totalSpent)}
                    </td>
                    <td className="px-3 py-2.5">
                      <TierBadge pts={c.loyaltyPoints} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between border-t border-[var(--border)] px-4 py-2.5 text-xs text-[var(--muted)]">
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

      {/* ── RIGHT: detail panel ── */}
      {selectedCustomer && (
        <div className="hidden border-l border-[var(--border)] bg-[var(--surface-solid)] md:flex md:w-[40%] lg:w-[35%] flex-col">
          <CustomerDetailPanel
            customer={selectedCustomer}
            onEdit={() => setFormModal(selectedCustomer)}
            onDelete={() => setDeleteTarget(selectedCustomer)}
            onClose={() => setSelected(null)}
          />
        </div>
      )}

      {/* ── FORM MODAL ── */}
      {formModal !== null && (
        <CustomerFormModal
          mode={formModal === 'create' ? 'create' : 'update'}
          initial={formModal === 'create' ? null : {
            customerId: formModal.customerId,
            fullName:   formModal.fullName,
            phone:      formModal.phone ?? '',
            gender:     formModal.gender ?? 'MALE',
            note:       formModal.note ?? '',
          }}
          storeId={currentStoreId}
          onClose={() => setFormModal(null)}
          onSuccess={() => {
            invalidate()
            // Nếu đang edit customer đang xem → close panel
            if (formModal !== 'create') setSelected(null)
          }}
        />
      )}

      {/* ── DELETE CONFIRM ── */}
      {deleteTarget && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-4 backdrop-blur-sm">
          <div className="w-full max-w-sm rounded-2xl border border-[var(--border)]
            bg-[var(--surface-solid)] p-5 shadow-2xl text-center">
            <span className="text-3xl">⚠️</span>
            <p className="mt-3 text-sm font-bold text-[var(--text)]">Xác nhận xóa khách hàng?</p>
            <p className="mt-1 text-xs text-[var(--muted)]">
              <span className="font-semibold text-[var(--text)]">{deleteTarget.fullName}</span> sẽ bị xóa khỏi hệ thống.
              Thao tác này không thể hoàn tác.
            </p>
            {deleteMutation.isError && (
              <p className="mt-2 rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] text-rose-500">
                ⚠ {deleteMutation.error?.message ?? 'Có lỗi xảy ra'}
              </p>
            )}
            <div className="mt-4 flex gap-2">
              <button type="button" onClick={() => setDeleteTarget(null)}
                className="flex-1 rounded-lg border border-[var(--border)] py-2 text-xs
                  text-[var(--text)] hover:bg-[var(--surface-2)] transition-colors">
                Hủy
              </button>
              <button type="button"
                onClick={() => deleteMutation.mutate(deleteTarget.customerId)}
                disabled={deleteMutation.isPending}
                className="flex flex-1 items-center justify-center gap-1.5 rounded-lg
                  bg-rose-500 py-2 text-xs font-bold text-white
                  disabled:opacity-60 hover:bg-rose-600 transition-colors">
                {deleteMutation.isPending && (
                  <span className="h-3 w-3 rounded-full border-2 border-white border-t-transparent animate-spin" />
                )}
                Xóa
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}