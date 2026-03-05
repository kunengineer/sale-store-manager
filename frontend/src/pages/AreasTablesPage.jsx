import { useState, useMemo } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useStore } from '../store/StoreContext'
import { getStoreLayout, createZone, updateZone } from '../data/services/storeZoneApi'
import { createTable, updateTable } from '../data/services/storeTableService'

// ── CONSTANTS ─────────────────────────────────────────────────
const ZONE_TYPES = [
  { value: 'FLOOR',   label: 'Khu tầng / phục vụ chung', icon: '🏢' },
  { value: 'ROOM',    label: 'Phòng riêng / VIP',         icon: '🚪' },
  { value: 'OUTDOOR', label: 'Ngoài trời / sân vườn',     icon: '🌿' },
  { value: 'BAR',     label: 'Quầy bar / pha chế',        icon: '🍹' },
  { value: 'CASHIER', label: 'Quầy thu ngân',              icon: '💳' },
  { value: 'STORAGE', label: 'Kho / hậu cần',              icon: '📦' },
]

// Màu solid rõ ràng thay vì opacity thấp
const TABLE_STATUSES = [
  {
    value: 'AVAILABLE',
    label: 'Trống',
    cardBg:  'bg-emerald-50 border-emerald-300 dark:bg-emerald-950 dark:border-emerald-700',
    badgeBg: 'bg-emerald-500 text-white',
    text:    'text-emerald-700 dark:text-emerald-300',
    dot:     'bg-emerald-500',
  },
  {
    value: 'OCCUPIED',
    label: 'Có khách',
    cardBg:  'bg-amber-50 border-amber-300 dark:bg-amber-950 dark:border-amber-700',
    badgeBg: 'bg-amber-500 text-white',
    text:    'text-amber-700 dark:text-amber-300',
    dot:     'bg-amber-500',
  },
  {
    value: 'RESERVED',
    label: 'Đã đặt',
    cardBg:  'bg-blue-50 border-blue-300 dark:bg-blue-950 dark:border-blue-700',
    badgeBg: 'bg-blue-500 text-white',
    text:    'text-blue-700 dark:text-blue-300',
    dot:     'bg-blue-500',
  },
  {
    value: 'MERGED',
    label: 'Đã gộp',
    cardBg:  'bg-purple-50 border-purple-300 dark:bg-purple-950 dark:border-purple-700',
    badgeBg: 'bg-purple-500 text-white',
    text:    'text-purple-700 dark:text-purple-300',
    dot:     'bg-purple-500',
  },
  {
    value: 'INACTIVE',
    label: 'Tạm khóa',
    cardBg:  'bg-slate-100 border-slate-300 dark:bg-slate-800 dark:border-slate-600',
    badgeBg: 'bg-slate-400 text-white',
    text:    'text-slate-500',
    dot:     'bg-slate-400',
  },
]

const getZoneIcon    = (t) => ZONE_TYPES.find(z => z.value === t)?.icon ?? '🏢'
const getZoneLabel   = (t) => ZONE_TYPES.find(z => z.value === t)?.label ?? t
const getTableStatus = (s) =>
  TABLE_STATUSES.find(x => x.value === (s ?? '').toUpperCase()) ?? TABLE_STATUSES[0]

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

function Sel({ error, children, ...props }) {
  return (
    <select
      className={`w-full rounded-lg border px-2.5 py-1.5 text-xs text-[var(--text)] bg-[var(--input)]
        focus:outline-none focus:ring-2 transition-all
        ${error ? 'border-rose-400 focus:ring-rose-400/30'
                : 'border-[var(--border)] focus:ring-emerald-500/30 focus:border-emerald-500'}`}
      {...props}
    >{children}</select>
  )
}

function Modal({ title, onClose, onSubmit, loading, children }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-4 py-6 backdrop-blur-sm">
      <div className="w-full max-w-md rounded-2xl border border-[var(--border)] bg-[var(--surface-solid)] p-5 shadow-2xl">
        <div className="mb-4 flex items-center justify-between">
          <h3 className="text-sm font-bold text-[var(--text)]">{title}</h3>
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px] text-[var(--muted)]
              hover:border-rose-400 hover:text-rose-400 transition-all">
            ✕ Đóng
          </button>
        </div>
        <div className="space-y-3">{children}</div>
        <div className="mt-5 flex justify-end gap-2">
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-4 py-1.5 text-xs text-[var(--text)]
              hover:bg-[var(--surface-2)] transition-colors">
            Hủy
          </button>
          <button type="button" onClick={onSubmit} disabled={loading}
            className="flex items-center gap-1.5 rounded-lg bg-emerald-500 px-5 py-1.5
              text-xs font-bold text-white disabled:opacity-60 hover:bg-emerald-600 transition-colors">
            {loading && <span className="h-3 w-3 rounded-full border-2 border-white border-t-transparent animate-spin" />}
            {loading ? 'Đang lưu...' : 'Lưu'}
          </button>
        </div>
      </div>
    </div>
  )
}

// ── TABLE CARD ────────────────────────────────────────────────
function TableCard({ table, onEdit }) {
  const st = getTableStatus(table.status)
  return (
    <button type="button" onClick={() => onEdit(table)}
      className={`group relative flex flex-col rounded-xl border-2 p-2.5 text-left
        transition-all duration-150 hover:-translate-y-0.5 hover:shadow-lg active:scale-95
        ${st.cardBg}`}>
      {/* Dot trạng thái góc trên phải */}
      <span className={`absolute right-2 top-2 h-2.5 w-2.5 rounded-full ${st.dot} shadow-sm`} />

      {/* Tên bàn */}
      <span className={`mb-1 pr-4 text-sm font-black tracking-tight ${st.text}`}>
        {table.tableCode}
      </span>

      {/* Badge trạng thái */}
      <span className={`mb-1.5 inline-flex w-fit rounded-full px-1.5 py-0.5 text-[9px] font-bold ${st.badgeBg}`}>
        {st.label}
      </span>

      {/* Số ghế */}
      <span className="text-[10px] font-medium text-[var(--muted)]">🪑 {table.seats} ghế</span>

      {/* Hover hint */}
      <span className="mt-0.5 text-[9px] text-transparent group-hover:text-[var(--muted)]/60 transition-all">
        Chỉnh sửa
      </span>
    </button>
  )
}

// ── ZONE CARD ─────────────────────────────────────────────────
function ZoneCard({ zone, onEditZone, onAddTable, onEditTable }) {
  const [collapsed, setCollapsed] = useState(false)
  const tableCount   = zone.tables?.length ?? 0
  const occupiedCount = zone.tables?.filter(t => (t.status ?? '').toUpperCase() === 'OCCUPIED').length ?? 0

  return (
    <div className="overflow-hidden rounded-2xl border border-[var(--border)] bg-[var(--surface-solid)] shadow-sm">
      {/* Header */}
      <div className="flex flex-wrap items-center gap-2 border-b border-[var(--border)] bg-[var(--surface-2)] px-4 py-3">
        <button type="button" onClick={() => setCollapsed(c => !c)}
          className="flex flex-1 items-center gap-2.5 text-left min-w-0">
          <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl
            bg-[var(--surface-solid)] border border-[var(--border)] text-lg shadow-sm">
            {getZoneIcon(zone.zoneType)}
          </span>
          <div className="min-w-0 flex-1">
            <div className="flex flex-wrap items-center gap-2">
              <p className="truncate text-sm font-bold text-[var(--text)]">{zone.zoneName}</p>
              {occupiedCount > 0 && (
                <span className="rounded-full bg-amber-500 px-2 py-0.5 text-[9px] font-bold text-white">
                  {occupiedCount} có khách
                </span>
              )}
            </div>
            <p className="text-[10px] font-medium text-[var(--muted)]">
              {getZoneLabel(zone.zoneType)} · {tableCount} bàn
            </p>
          </div>
          <span className={`shrink-0 text-xs text-[var(--muted)] transition-transform duration-200
            ${collapsed ? '-rotate-90' : 'rotate-0'}`}>▾</span>
        </button>

        <div className="flex shrink-0 items-center gap-1.5">
          <button type="button" onClick={() => onAddTable(zone)}
            className="rounded-lg border border-emerald-500 bg-emerald-500/10 px-3 py-1.5
              text-[11px] font-semibold text-emerald-600 dark:text-emerald-400
              hover:bg-emerald-500 hover:text-white transition-all duration-150">
            + Bàn
          </button>
          <button type="button" onClick={() => onEditZone(zone)}
            className="rounded-lg border border-[var(--border)] px-3 py-1.5 text-[11px]
              font-medium text-[var(--muted)] hover:border-[var(--text)] hover:text-[var(--text)]
              transition-all duration-150">
            Sửa
          </button>
        </div>
      </div>

      {/* Tables
          Dùng auto-fill + minmax(120px, 1fr):
          - Tự tính số cột theo chiều ngang container
          - Không bao giờ tràn ngang — wrap xuống hàng mới
          - Mobile (~360px) → 2 cột | tablet → 4 cột | desktop → 6+ cột */}
      {!collapsed && (
        <div className="p-3">
          {tableCount === 0 ? (
            <div className="flex flex-col items-center justify-center py-6 gap-2 text-center">
              <span className="text-3xl opacity-30">🪑</span>
              <p className="text-[11px] text-[var(--muted)]/60">
                Chưa có bàn · Nhấn &quot;+ Bàn&quot; để thêm
              </p>
            </div>
          ) : (
            <div className="grid gap-2"
              style={{ gridTemplateColumns: 'repeat(auto-fill, minmax(120px, 1fr))' }}>
              {zone.tables.map((t) => (
                <TableCard key={t.tableId} table={t} onEdit={(tbl) => onEditTable(tbl, zone)} />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  )
}

// ── MAIN PAGE ─────────────────────────────────────────────────
export function AreasTablesPage() {
  const { currentStoreId } = useStore()
  const queryClient = useQueryClient()

  const { data: layout, isLoading } = useQuery({
    queryKey: ['storeLayout', currentStoreId],
    queryFn: () => getStoreLayout(currentStoreId),
    enabled: !!currentStoreId,
    staleTime: 1000 * 60 * 2,
  })

  const zones = useMemo(() => Array.isArray(layout?.data) ? layout.data : [], [layout])

  const invalidateLayout = () =>
    queryClient.invalidateQueries({ queryKey: ['storeLayout', currentStoreId] })

  const [zoneModal, setZoneModal]   = useState(null)
  const [tableModal, setTableModal] = useState(null)

  const [zoneForm, setZoneForm]     = useState({ zoneName: '', zoneType: 'FLOOR', capacity: '', isActive: true })
  const [zoneErrors, setZoneErrors] = useState({})

  const openCreateZone = () => {
    setZoneForm({ zoneName: '', zoneType: 'FLOOR', capacity: '', isActive: true })
    setZoneErrors({})
    setZoneModal('create')
  }

  const openEditZone = (zone) => {
    setZoneForm({ zoneName: zone.zoneName, zoneType: zone.zoneType ?? 'FLOOR',
      capacity: zone.capacity ?? '', isActive: zone.isActive ?? true })
    setZoneErrors({})
    setZoneModal(zone)
  }

  const zoneMutation = useMutation({
    mutationFn: (data) => zoneModal === 'create'
      ? createZone({ storeId: currentStoreId, ...data })
      : updateZone(zoneModal.zoneId, { storeId: currentStoreId, ...data }),
    onSuccess: () => { invalidateLayout(); setZoneModal(null) },
  })

  const handleZoneSubmit = () => {
    const e = {}
    if (!zoneForm.zoneName.trim()) e.zoneName = 'Vui lòng nhập tên khu vực'
    if (Object.keys(e).length) { setZoneErrors(e); return }
    zoneMutation.mutate(zoneForm)
  }

  const [tableForm, setTableForm]     = useState({ tableCode: '', seats: 4, status: 'AVAILABLE', isActive: true })
  const [tableErrors, setTableErrors] = useState({})

  const openAddTable = (zone) => {
    setTableForm({ tableCode: '', seats: 4, status: 'AVAILABLE', isActive: true })
    setTableErrors({})
    setTableModal({ mode: 'create', zone })
  }

  const openEditTable = (table, zone) => {
    setTableForm({ tableCode: table.tableCode, seats: table.seats,
      status: (table.status ?? 'AVAILABLE').toUpperCase(), isActive: table.isActive ?? true })
    setTableErrors({})
    setTableModal({ mode: 'edit', table, zone })
  }

  const tableMutation = useMutation({
    mutationFn: (data) => tableModal.mode === 'create'
      ? createTable({ storeZoneId: tableModal.zone.zoneId, ...data })
      : updateTable(tableModal.table.tableId, { storeZoneId: tableModal.zone.zoneId, ...data }),
    onSuccess: () => { invalidateLayout(); setTableModal(null) },
  })

  const handleTableSubmit = () => {
    const e = {}
    if (!tableForm.tableCode.trim()) e.tableCode = 'Vui lòng nhập mã bàn'
    if (!tableForm.seats || tableForm.seats < 1) e.seats = 'Số ghế phải lớn hơn 0'
    if (Object.keys(e).length) { setTableErrors(e); return }
    tableMutation.mutate({ ...tableForm, seats: Number(tableForm.seats) })
  }

  const stats = useMemo(() => {
    const all = zones.flatMap(z => z.tables ?? [])
    return {
      zones:     zones.length,
      tables:    all.length,
      occupied:  all.filter(t => (t.status ?? '').toUpperCase() === 'OCCUPIED').length,
      available: all.filter(t => (t.status ?? '').toUpperCase() === 'AVAILABLE').length,
    }
  }, [zones])

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-3 py-4 sm:px-5 text-[var(--text)]">

      {/* Header */}
      <div className="mb-4 flex flex-wrap items-start justify-between gap-3">
        <div>
          <h1 className="text-base font-bold sm:text-lg text-[var(--text)]">Khu vực &amp; Bàn</h1>
          <p className="text-xs text-[var(--muted)]">Quản lý sơ đồ khu vực, bàn và trạng thái phục vụ</p>
        </div>
        <button type="button" onClick={openCreateZone}
          className="rounded-xl bg-emerald-500 px-4 py-2 text-xs font-bold text-white
            hover:bg-emerald-600 active:scale-95 transition-all shadow-sm shadow-emerald-500/30">
          + Thêm khu vực
        </button>
      </div>

      {/* Stats — 2 cột mobile, 4 cột desktop */}
      <div className="mb-4 grid grid-cols-2 gap-2 sm:grid-cols-4">
        {[
          { label: 'Khu vực',  value: stats.zones,    valCls: 'text-[var(--text)]',                           bgCls: 'bg-[var(--surface-solid)]' },
          { label: 'Tổng bàn', value: stats.tables,   valCls: 'text-[var(--text)]',                           bgCls: 'bg-[var(--surface-solid)]' },
          { label: 'Có khách', value: stats.occupied,  valCls: 'text-amber-600 dark:text-amber-400',           bgCls: 'bg-amber-50 dark:bg-amber-950/50' },
          { label: 'Trống',    value: stats.available, valCls: 'text-emerald-600 dark:text-emerald-400',       bgCls: 'bg-emerald-50 dark:bg-emerald-950/50' },
        ].map(s => (
          <div key={s.label}
            className={`rounded-xl border border-[var(--border)] ${s.bgCls} px-3 py-3 text-center shadow-sm`}>
            <p className={`text-2xl font-black ${s.valCls}`}>{s.value}</p>
            <p className="mt-0.5 text-[10px] font-semibold text-[var(--muted)]">{s.label}</p>
          </div>
        ))}
      </div>

      {/* Legend */}
      <div className="mb-3 flex flex-wrap gap-3">
        {TABLE_STATUSES.map(s => (
          <span key={s.value} className="flex items-center gap-1.5 text-[10px] font-semibold text-[var(--muted)]">
            <span className={`h-2 w-2 rounded-full ${s.dot}`} />{s.label}
          </span>
        ))}
      </div>

      {/* Content */}
      {isLoading ? (
        <div className="flex items-center justify-center gap-2 py-20 text-sm text-[var(--muted)]">
          <span className="h-4 w-4 animate-spin rounded-full border-2 border-[var(--border)] border-t-emerald-500" />
          Đang tải dữ liệu...
        </div>
      ) : zones.length === 0 ? (
        <div className="flex flex-col items-center justify-center gap-3 py-24">
          <span className="text-5xl opacity-30">🗺️</span>
          <p className="text-sm font-medium text-[var(--muted)]">Chưa có khu vực nào</p>
          <button type="button" onClick={openCreateZone}
            className="rounded-xl bg-emerald-500 px-5 py-2 text-xs font-bold text-white hover:bg-emerald-600">
            + Tạo khu vực đầu tiên
          </button>
        </div>
      ) : (
        <div className="space-y-3">
          {zones.map(zone => (
            <ZoneCard key={zone.zoneId} zone={zone}
              onEditZone={openEditZone} onAddTable={openAddTable} onEditTable={openEditTable} />
          ))}
        </div>
      )}

      {/* Zone Modal */}
      {zoneModal !== null && (
        <Modal
          title={zoneModal === 'create' ? '+ Thêm khu vực mới' : `Chỉnh sửa · ${zoneModal.zoneName}`}
          onClose={() => setZoneModal(null)} onSubmit={handleZoneSubmit} loading={zoneMutation.isPending}>
          <Field label="Tên khu vực *" error={zoneErrors.zoneName}>
            <Inp placeholder="VD: Tầng 1, Phòng VIP..." value={zoneForm.zoneName}
              onChange={e => setZoneForm(f => ({ ...f, zoneName: e.target.value }))} error={zoneErrors.zoneName} />
          </Field>
          <Field label="Loại khu vực">
            <Sel value={zoneForm.zoneType} onChange={e => setZoneForm(f => ({ ...f, zoneType: e.target.value }))}>
              {ZONE_TYPES.map(z => <option key={z.value} value={z.value}>{z.icon} {z.label}</option>)}
            </Sel>
          </Field>
          <Field label="Sức chứa (người)">
            <Inp type="number" placeholder="VD: 50" value={zoneForm.capacity}
              onChange={e => setZoneForm(f => ({ ...f, capacity: e.target.value }))} />
          </Field>
          <label className="flex cursor-pointer items-center gap-2 text-xs font-medium text-[var(--text)]">
            <input type="checkbox" checked={zoneForm.isActive}
              onChange={e => setZoneForm(f => ({ ...f, isActive: e.target.checked }))}
              className="h-3.5 w-3.5 accent-emerald-500" />
            Khu vực đang hoạt động
          </label>
          {zoneMutation.isError && (
            <p className="rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] font-medium text-rose-500">
              ⚠ {zoneMutation.error?.message}
            </p>
          )}
        </Modal>
      )}

      {/* Table Modal */}
      {tableModal !== null && (
        <Modal
          title={tableModal.mode === 'create'
            ? `+ Thêm bàn · ${tableModal.zone.zoneName}`
            : `Chỉnh sửa · ${tableModal.table?.tableCode}`}
          onClose={() => setTableModal(null)} onSubmit={handleTableSubmit} loading={tableMutation.isPending}>
          <Field label="Mã bàn *" error={tableErrors.tableCode}>
            <Inp placeholder="VD: A1, T01, VIP-01..." value={tableForm.tableCode}
              onChange={e => setTableForm(f => ({ ...f, tableCode: e.target.value }))} error={tableErrors.tableCode} />
          </Field>
          <Field label="Số ghế *" error={tableErrors.seats}>
            <Inp type="number" min={1} value={tableForm.seats}
              onChange={e => setTableForm(f => ({ ...f, seats: e.target.value }))} error={tableErrors.seats} />
          </Field>
          {tableModal.mode === 'edit' && (
            <Field label="Trạng thái">
              <Sel value={tableForm.status} onChange={e => setTableForm(f => ({ ...f, status: e.target.value }))}>
                {TABLE_STATUSES.map(s => <option key={s.value} value={s.value}>{s.label}</option>)}
              </Sel>
            </Field>
          )}
          <label className="flex cursor-pointer items-center gap-2 text-xs font-medium text-[var(--text)]">
            <input type="checkbox" checked={tableForm.isActive}
              onChange={e => setTableForm(f => ({ ...f, isActive: e.target.checked }))}
              className="h-3.5 w-3.5 accent-emerald-500" />
            Bàn đang hoạt động
          </label>
          {tableMutation.isError && (
            <p className="rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] font-medium text-rose-500">
              ⚠ {tableMutation.error?.message}
            </p>
          )}
        </Modal>
      )}
    </div>
  )
}