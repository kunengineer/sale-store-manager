import { useMemo, useState } from 'react'
import { ADMIN_METRICS, ADMIN_STORES, ADMIN_USERS } from '../data/mockAdminData'

export function AdminPage() {
  const [tab, setTab] = useState('dashboard')
  const tabs = useMemo(
    () => [
      { key: 'dashboard', label: 'Tổng quan' },
      { key: 'stores', label: 'Cửa hàng' },
      { key: 'users', label: 'Người dùng' },
    ],
    [],
  )

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3 text-[var(--text)]">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold">Super Admin</h1>
          <p className="text-xs text-[var(--muted)]">
            Quản trị hệ thống multi-tenant (mock data, sẵn sàng nối API).
          </p>
        </div>
      </div>

      <div className="mb-3 flex gap-2 text-xs">
        {tabs.map((t) => (
          <button
            key={t.key}
            type="button"
            onClick={() => setTab(t.key)}
            className={`rounded-full px-3 py-1 ${
              tab === t.key
                ? 'bg-emerald-500 text-slate-950 font-semibold'
                : 'border border-[var(--border)] bg-[var(--surface-solid)] text-[var(--text)]'
            }`}
          >
            {t.label}
          </button>
        ))}
      </div>

      {tab === 'dashboard' && (
        <div className="grid gap-3 md:grid-cols-4">
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
            <p className="text-[11px] text-[var(--muted)]">Tổng cửa hàng</p>
            <p className="mt-1 text-lg font-semibold">{ADMIN_METRICS.totalStores}</p>
          </div>
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
            <p className="text-[11px] text-[var(--muted)]">Đang hoạt động</p>
            <p className="mt-1 text-lg font-semibold">{ADMIN_METRICS.activeStores}</p>
          </div>
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
            <p className="text-[11px] text-[var(--muted)]">Tổng người dùng</p>
            <p className="mt-1 text-lg font-semibold">{ADMIN_METRICS.totalUsers}</p>
          </div>
          <div className="rounded-xl border border-emerald-500/40 bg-emerald-500/10 p-3">
            <p className="text-[11px] text-emerald-700">Doanh thu hôm nay</p>
            <p className="mt-1 text-lg font-semibold text-emerald-600">
              {ADMIN_METRICS.todayRevenue.toLocaleString('vi-VN')}đ
            </p>
          </div>
        </div>
      )}

      {tab === 'stores' && (
        <div className="overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--surface-solid)]">
          <table className="min-w-full text-left text-xs">
            <thead className="bg-[var(--surface)] text-[var(--muted)]">
              <tr>
                <th className="px-3 py-2">Cửa hàng</th>
                <th className="px-3 py-2">Gói</th>
                <th className="px-3 py-2">Trạng thái</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {ADMIN_STORES.map((s) => (
                <tr key={s.id}>
                  <td className="px-3 py-2 font-semibold">{s.name}</td>
                  <td className="px-3 py-2">{s.plan}</td>
                  <td className="px-3 py-2">
                    <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[11px] text-emerald-700">
                      {s.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {tab === 'users' && (
        <div className="overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--surface-solid)]">
          <table className="min-w-full text-left text-xs">
            <thead className="bg-[var(--surface)] text-[var(--muted)]">
              <tr>
                <th className="px-3 py-2">Tên</th>
                <th className="px-3 py-2">Email</th>
                <th className="px-3 py-2">Vai trò</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {ADMIN_USERS.map((u) => (
                <tr key={u.id}>
                  <td className="px-3 py-2 font-semibold">{u.name}</td>
                  <td className="px-3 py-2">{u.email}</td>
                  <td className="px-3 py-2">{u.role}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

