import { useMemo, useState } from 'react'

const INITIAL_AREAS = [
  { id: 'a1', name: 'Tầng 1' },
  { id: 'a2', name: 'Tầng 2' },
]

const INITIAL_TABLES = [
  { id: 't1', areaId: 'a1', name: 'A1', seats: 4, status: 'empty' },
  { id: 't2', areaId: 'a1', name: 'A2', seats: 4, status: 'using' },
  { id: 't3', areaId: 'a2', name: 'B1', seats: 4, status: 'empty' },
]

export function AreasTablesPage() {
  const [areas, setAreas] = useState(INITIAL_AREAS)
  const [tables, setTables] = useState(INITIAL_TABLES)
  const [activeAreaId, setActiveAreaId] = useState(INITIAL_AREAS[0].id)
  const [showAreaForm, setShowAreaForm] = useState(false)
  const [showTableForm, setShowTableForm] = useState(false)
  const [areaName, setAreaName] = useState('')
  const [tableForm, setTableForm] = useState({
    name: '',
    seats: 4,
    areaId: INITIAL_AREAS[0].id,
  })

  const tablesForArea = useMemo(
    () => tables.filter((t) => t.areaId === activeAreaId),
    [tables, activeAreaId],
  )

  const handleAddArea = (e) => {
    e.preventDefault()
    if (!areaName) return
    const id = crypto.randomUUID()
    setAreas((prev) => [...prev, { id, name: areaName }])
    setAreaName('')
    setShowAreaForm(false)
  }

  const handleAddTable = (e) => {
    e.preventDefault()
    if (!tableForm.name) return
    setTables((prev) => [
      ...prev,
      {
        id: crypto.randomUUID(),
        name: tableForm.name,
        seats: Number(tableForm.seats) || 4,
        areaId: tableForm.areaId,
        status: 'empty',
      },
    ])
    setTableForm({ ...tableForm, name: '' })
    setShowTableForm(false)
  }

  return (
    <>
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3 text-[var(--text)]">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">
            Khu vực &amp; Bàn
          </h1>
          <p className="text-xs text-[var(--muted)]">
            Quản lý sơ đồ khu vực, bàn, số ghế và trạng thái.
          </p>
        </div>
        <div className="flex gap-2">
          <button
            type="button"
            className="rounded-lg border border-[var(--border)] bg-[var(--surface-solid)] px-3 py-1.5 text-xs text-[var(--text)]"
            onClick={() => setShowAreaForm(true)}
          >
            + Khu vực
          </button>
          <button
            type="button"
            className="rounded-lg bg-emerald-500 px-3 py-1.5 text-xs font-semibold text-slate-950"
            onClick={() => setShowTableForm(true)}
          >
            + Bàn
          </button>
        </div>
      </div>

      <div className="grid gap-4 md:grid-cols-[1.1fr,1.4fr]">
        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <h2 className="mb-2 text-xs font-semibold uppercase tracking-wide text-[var(--muted)]">
            Khu vực
          </h2>
          <ul className="space-y-2 text-xs text-[var(--text)]">
            {areas.map((a) => (
              <li
                key={a.id}
                className={`flex cursor-pointer items-center justify-between rounded-lg px-3 py-2 ${
                  a.id === activeAreaId
                    ? 'bg-[var(--surface-2)]'
                    : 'bg-[var(--surface-solid)]'
                }`}
                onClick={() => setActiveAreaId(a.id)}
              >
                <span>{a.name}</span>
                <span className="text-[11px] text-[var(--muted)]">
                  {
                    tables.filter((t) => t.areaId === a.id)
                      .length
                  }{' '}
                  bàn
                </span>
              </li>
            ))}
          </ul>
        </div>

        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <div className="mb-2 flex items-center justify-between text-xs">
            <span className="font-semibold text-[var(--text)]">
              Bàn · Khu vực:{' '}
              {areas.find((a) => a.id === activeAreaId)?.name ?? ''}
            </span>
            <select
              className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1 text-[11px] text-[var(--text)]"
              value={activeAreaId}
              onChange={(e) => setActiveAreaId(e.target.value)}
            >
              {areas.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.name}
                </option>
              ))}
            </select>
          </div>


          <div className="grid grid-cols-4 gap-2 text-xs">
            {tablesForArea.map((t) => (
              <button
                key={t.id}
                type="button"
                className="flex flex-col items-start rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-2 py-2 text-left"
              >
                <span className="text-sm font-semibold text-[var(--text)]">
                  Bàn {t.name}
                </span>
                <span className="mt-1 text-[11px] text-[var(--muted)]">
                  {t.seats} ghế ·{' '}
                  {t.status === 'using' ? 'Đang dùng' : 'Trống'}
                </span>
              </button>
            ))}
          </div>
        </div>
      </div>
    </div>
      {showAreaForm && (
        <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/50">
          <form
            onSubmit={handleAddArea}
            className="w-full max-w-sm rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-4 text-xs text-[var(--text)]"
          >
            <h3 className="mb-2 text-sm font-semibold text-[var(--text)]">
              Thêm khu vực
            </h3>
            <label className="mb-2 block text-[11px] text-[var(--muted)]">
              Tên khu vực
              <input
                className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                placeholder="Ví dụ: Khu VIP"
                value={areaName}
                onChange={(e) => setAreaName(e.target.value)}
              />
            </label>
            <div className="mt-3 flex justify-end gap-2">
              <button
                type="button"
                className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-3 py-1.5 text-[11px] text-[var(--text)]"
                onClick={() => setShowAreaForm(false)}
              >
                Hủy
              </button>
              <button
                type="submit"
                className="rounded-lg bg-emerald-500 px-3 py-1.5 text-[11px] font-semibold text-slate-950"
              >
                Lưu
              </button>
            </div>
          </form>
        </div>
      )}
      {showTableForm && (
        <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/50">
          <form
            onSubmit={handleAddTable}
            className="w-full max-w-lg rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-4 text-xs text-[var(--text)]"
          >
            <h3 className="mb-2 text-sm font-semibold text-[var(--text)]">
              Thêm bàn
            </h3>
            <div className="grid gap-3 md:grid-cols-3">
              <label className="block text-[11px] text-[var(--muted)]">
                Tên bàn
                <input
                  className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                  value={tableForm.name}
                  onChange={(e) =>
                    setTableForm((f) => ({ ...f, name: e.target.value }))
                  }
                />
              </label>
              <label className="block text-[11px] text-[var(--muted)]">
                Số ghế
                <input
                  type="number"
                  className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                  value={tableForm.seats}
                  onChange={(e) =>
                    setTableForm((f) => ({ ...f, seats: e.target.value }))
                  }
                />
              </label>
              <label className="block text-[11px] text-[var(--muted)]">
                Khu vực
                <select
                  className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                  value={tableForm.areaId}
                  onChange={(e) =>
                    setTableForm((f) => ({ ...f, areaId: e.target.value }))
                  }
                >
                  {areas.map((a) => (
                    <option key={a.id} value={a.id}>
                      {a.name}
                    </option>
                  ))}
                </select>
              </label>
            </div>
            <div className="mt-3 flex justify-end gap-2">
              <button
                type="button"
                className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-3 py-1.5 text-[11px] text-[var(--text)]"
                onClick={() => setShowTableForm(false)}
              >
                Hủy
              </button>
              <button
                type="submit"
                className="rounded-lg bg-emerald-500 px-3 py-1.5 text-[11px] font-semibold text-slate-950"
              >
                Lưu
              </button>
            </div>
          </form>
        </div>
      )}
    </>
  )
}

