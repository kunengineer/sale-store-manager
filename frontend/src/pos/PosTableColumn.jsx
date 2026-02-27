import { useState } from 'react'
import { usePos } from './PosContext'
import { useStore } from '../store/StoreContext'

const STATUS_FILTERS = [
  { key: 'ALL', label: 'Tất cả' },
  { key: 'USING', label: 'Có khách' },
  { key: 'EMPTY', label: 'Trống' },
]

export function PosTableColumn() {
  const { areas, allTables, selectedTable, selectTable } = usePos()
  const [statusFilter, setStatusFilter] = useState('ALL')
  const { theme } = useStore()

  const filterTables = (tables) =>
    tables.filter((t) => {
      if (statusFilter === 'USING') return t.status === 'using'
      if (statusFilter === 'EMPTY') return t.status !== 'using'
      return true
    })

  const outerClass =
    theme === 'dark'
      ? 'border-[var(--border)] bg-[var(--surface)]'
      : 'border-[var(--border)] bg-[var(--surface-solid)]'

  return (
    <section
      className={`flex w-[24%] min-w-[230px] max-w-[280px] flex-col rounded-xl border ${outerClass}`}
    >
      <div className="flex gap-2 border-b border-[var(--border)] px-3 py-2 text-[11px]">
        {STATUS_FILTERS.map((f) => (
          <button
            key={f.key}
            type="button"
            onClick={() => setStatusFilter(f.key)}
            className={`flex-1 rounded-full px-2 py-1 ${
              statusFilter === f.key
                ? 'bg-emerald-500 text-slate-950 font-semibold'
                : 'bg-[var(--surface-2)] text-[var(--text)]'
            }`}
          >
            {f.label}
          </button>
        ))}
      </div>

      <div className="flex-1 overflow-auto px-2.5 py-2 space-y-3">
        {areas.map((area) => {
          const tables = filterTables(
            allTables.filter((t) => t.areaId === area.id),
          )
          if (!tables.length) return null
          return (
            <div key={area.id} className="rounded-xl border border-[var(--border)]">
              <div className="flex items-center justify-between rounded-t-xl bg-emerald-700 px-3 py-1.5 text-[11px] font-semibold text-white">
                <span>{area.name}</span>
                <span className="text-white/80">
                  {tables.length} bàn
                </span>
              </div>
              <div className="grid grid-cols-3 gap-1.5 p-2">
                {tables.map((table) => {
                  const isUsing = table.status === 'using'
                  const isSelected = selectedTable?.id === table.id
                  const timeText = (() => {
                    const src =
                      table.status === 'using'
                        ? table.openedAt
                        : table.status === 'reserved'
                          ? table.reservedAt
                          : null
                    if (!src) return null
                    const d = new Date(src)
                    const hhmm = d.toLocaleTimeString('vi-VN', {
                      hour: '2-digit',
                      minute: '2-digit',
                    })
                    return table.status === 'using' ? `Vào ${hhmm}` : `Đặt ${hhmm}`
                  })()
                  return (
                    <button
                      key={table.id}
                      type="button"
                      onClick={() => selectTable(table.id)}
                      className={`flex h-16 flex-col items-center justify-center rounded-lg border text-[11px] ${
                        isUsing
                          ? 'border-emerald-500 bg-emerald-500/10'
                          : 'border-[var(--border)] bg-[var(--surface-2)]'
                      } ${isSelected ? 'ring-2 ring-emerald-500' : ''}`}
                    >
                      <span className="text-lg">☕</span>
                      <span className="mt-1 font-semibold text-[var(--text)]">
                        {table.name}
                      </span>
                      <span className="text-[10px] text-[var(--muted)]">
                        {isUsing ? 'Có khách' : 'Trống'}
                      </span>
                      {timeText && (
                        <span className="text-[10px] text-[var(--muted)]">
                          {timeText}
                        </span>
                      )}
                    </button>
                  )
                })}
              </div>
            </div>
          )
        })}
      </div>
    </section>
  )
}

