import { useMemo, useState } from 'react'

const INITIAL_CUSTOMERS = [
  {
    id: 'c1',
    name: 'Trần Minh An',
    phone: '0909 123 456',
    totalSpend: 12500000,
    lastVisit: '25/02/2026 · 19:30',
    tier: 'Gold',
    note: 'Khách quen',
  },
]

export function CustomersPage() {
  const [customers, setCustomers] = useState(INITIAL_CUSTOMERS)
  const [search, setSearch] = useState('')
  const [tierFilter, setTierFilter] = useState('Tất cả')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({
    name: '',
    phone: '',
    tier: 'Silver',
  })

  const filtered = useMemo(
    () =>
      customers.filter((c) => {
        const matchSearch =
          c.name.toLowerCase().includes(search.trim().toLowerCase()) ||
          c.phone.includes(search.trim())
        const matchTier =
          tierFilter === 'Tất cả' || c.tier === tierFilter
        return matchSearch && matchTier
      }),
    [customers, search, tierFilter],
  )

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!form.name || !form.phone) return
    setCustomers((prev) => [
      ...prev,
      {
        id: crypto.randomUUID(),
        name: form.name,
        phone: form.phone,
        totalSpend: 0,
        lastVisit: '-',
        tier: form.tier,
        note: '',
      },
    ])
    setForm({ name: '', phone: '', tier: 'Silver' })
    setShowForm(false)
  }

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">Khách hàng</h1>
          <p className="text-xs text-[var(--muted)]">
            CRM khách hàng: lưu lịch sử mua, số điện thoại, hạng thành viên.
          </p>
        </div>
        <button
          type="button"
          className="rounded-lg bg-emerald-500 px-3 py-1.5 text-xs font-semibold text-slate-950"
          onClick={() => setShowForm(true)}
        >
          + Khách hàng
        </button>
      </div>

      <div className="mb-3 flex flex-wrap gap-2 text-xs">
        <input
          className="w-56 rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-xs text-[var(--text)] placeholder:text-[var(--muted)]"
          placeholder="Tìm theo SĐT hoặc tên..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]"
          value={tierFilter}
          onChange={(e) => setTierFilter(e.target.value)}
        >
          <option>Tất cả</option>
          <option>Silver</option>
          <option>Gold</option>
          <option>Platinum</option>
        </select>
      </div>

      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="mb-3 grid gap-2 rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3 text-xs text-[var(--text)] md:grid-cols-3"
        >
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Tên khách hàng
            </label>
            <input
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.name}
              onChange={(e) =>
                setForm((f) => ({ ...f, name: e.target.value }))
              }
            />
          </div>
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Số điện thoại
            </label>
            <input
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.phone}
              onChange={(e) =>
                setForm((f) => ({ ...f, phone: e.target.value }))
              }
            />
          </div>
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Hạng
            </label>
            <select
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.tier}
              onChange={(e) =>
                setForm((f) => ({ ...f, tier: e.target.value }))
              }
            >
              <option>Silver</option>
              <option>Gold</option>
              <option>Platinum</option>
            </select>
          </div>
          <div className="md:col-span-3 flex justify-end gap-2">
            <button
              type="button"
              className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-3 py-1.5 text-[11px] text-[var(--text)]"
              onClick={() => setShowForm(false)}
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
      )}

      <div className="overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--surface-solid)]">
        <table className="min-w-full text-left text-xs text-[var(--text)]">
          <thead className="bg-[var(--surface)] text-[var(--muted)]">
            <tr>
              <th className="px-3 py-2">Khách hàng</th>
              <th className="px-3 py-2">Số điện thoại</th>
              <th className="px-3 py-2 text-right">Tổng chi tiêu</th>
              <th className="px-3 py-2">Lần cuối ghé</th>
              <th className="px-3 py-2">Hạng</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[var(--border)]">
            {filtered.map((c) => (
              <tr key={c.id}>
                <td className="px-3 py-2">
                  <div>
                    <p className="text-xs font-semibold text-[var(--text)]">
                      {c.name}
                    </p>
                    <p className="text-[11px] text-[var(--muted)]">{c.note}</p>
                  </div>
                </td>
                <td className="px-3 py-2 text-xs text-[var(--text)]">
                  {c.phone}
                </td>
                <td className="px-3 py-2 text-right text-xs font-semibold text-emerald-400">
                  {c.totalSpend.toLocaleString('vi-VN')}đ
                </td>
                <td className="px-3 py-2 text-xs text-[var(--text)]">
                  {c.lastVisit}
                </td>
                <td className="px-3 py-2">
                  <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[11px] text-emerald-300">
                    {c.tier}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

