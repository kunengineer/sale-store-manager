import { useMemo, useState } from 'react'

const INITIAL_STAFF = [
  {
    id: 's1',
    name: 'Ngọc Dương',
    phone: '0987 654 321',
    role: 'Thu ngân',
    status: 'active',
    createdAt: '01/03/2026',
  },
]

export function StaffPage() {
  const [staff, setStaff] = useState(INITIAL_STAFF)
  const [search, setSearch] = useState('')
  const [roleFilter, setRoleFilter] = useState('Tất cả')
  const [statusFilter, setStatusFilter] = useState('Đang làm việc')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({
    name: '',
    phone: '',
    role: 'Thu ngân',
    status: 'active',
  })

  const filtered = useMemo(
    () =>
      staff.filter((s) => {
        const matchSearch =
          s.name.toLowerCase().includes(search.trim().toLowerCase()) ||
          s.phone.includes(search.trim())
        const matchRole =
          roleFilter === 'Tất cả' || s.role === roleFilter
        const matchStatus =
          statusFilter === 'Tất cả'
            ? true
            : statusFilter === 'Đang làm việc'
              ? s.status === 'active'
              : s.status !== 'active'
        return matchSearch && matchRole && matchStatus
      }),
    [staff, search, roleFilter, statusFilter],
  )

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!form.name || !form.phone) return
    setStaff((prev) => [
      ...prev,
      {
        id: crypto.randomUUID(),
        ...form,
        createdAt: new Date().toLocaleDateString('vi-VN'),
      },
    ])
    setForm({ name: '', phone: '', role: 'Thu ngân', status: 'active' })
    setShowForm(false)
  }

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">Nhân viên</h1>
          <p className="text-xs text-[var(--muted)]">
            Quản lý tài khoản nhân viên theo từng cửa hàng, phân quyền thu ngân,
            phục vụ, quản lý.
          </p>
        </div>
        <button
          type="button"
          className="rounded-lg bg-emerald-500 px-3 py-1.5 text-xs font-semibold text-slate-950"
          onClick={() => setShowForm(true)}
        >
          + Nhân viên
        </button>
      </div>

      <div className="mb-3 flex flex-wrap gap-2 text-xs">
        <input
          className="w-56 rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-xs text-[var(--text)] placeholder:text-[var(--muted)]"
          placeholder="Tìm theo tên hoặc số điện thoại..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]"
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value)}
        >
          <option>Tất cả</option>
          <option>Thu ngân</option>
          <option>Phục vụ</option>
          <option>Pha chế</option>
        </select>
        <select
          className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option>Đang làm việc</option>
          <option>Đã nghỉ</option>
          <option>Tất cả</option>
        </select>
      </div>

      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="mb-3 grid gap-2 rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3 text-xs text-[var(--text)] md:grid-cols-4"
        >
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Họ và tên
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
              Vai trò
            </label>
            <select
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.role}
              onChange={(e) =>
                setForm((f) => ({ ...f, role: e.target.value }))
              }
            >
              <option>Thu ngân</option>
              <option>Phục vụ</option>
              <option>Pha chế</option>
            </select>
          </div>
          <div className="md:col-span-4 flex justify-end gap-2">
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
              <th className="px-3 py-2">Nhân viên</th>
              <th className="px-3 py-2">Số điện thoại</th>
              <th className="px-3 py-2">Vai trò</th>
              <th className="px-3 py-2">Trạng thái</th>
              <th className="px-3 py-2 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[var(--border)]">
            {filtered.map((s) => (
              <tr key={s.id}>
                <td className="px-3 py-2">
                  <div>
                    <p className="text-xs font-semibold text-[var(--text)]">
                      {s.name}
                    </p>
                    <p className="text-[11px] text-[var(--muted)]">
                      Tạo: {s.createdAt}
                    </p>
                  </div>
                </td>
                <td className="px-3 py-2 text-xs text-[var(--text)]">
                  {s.phone}
                </td>
                <td className="px-3 py-2 text-xs text-[var(--text)]">
                  {s.role}
                </td>
                <td className="px-3 py-2">
                  <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[11px] text-emerald-300">
                    {s.status === 'active' ? 'Đang làm việc' : 'Đã nghỉ'}
                  </span>
                </td>
                <td className="px-3 py-2 text-right text-xs">
                  <button className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-2 py-1 text-[11px] text-[var(--text)]">
                    Sửa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}

