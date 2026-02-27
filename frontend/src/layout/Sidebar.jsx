import { NavLink } from 'react-router-dom'
import { useStore } from '../store/StoreContext'

const NAV_ITEMS = [
  { label: 'Bán hàng', to: '/app/pos', icon: '▢▢' },
  { label: 'Khu vực & bàn', to: '/app/areas-tables', icon: '🪑' },
  { label: 'Khách hàng', to: '/app/customers', icon: '👤' },
  { label: 'Thực đơn', to: '/app/products', icon: '☕' },
  { label: 'Báo cáo', to: '/app/reports', icon: '📊' },
  { label: 'Lịch sử', to: '/app/orders', icon: '⏱' },
  { label: 'Admin', to: '/app/admin', icon: '🛡' },
]

export function Sidebar() {
  const { theme } = useStore()
  const bg =
    theme === 'dark'
      ? 'bg-emerald-900 text-emerald-50'
      : 'bg-emerald-600 text-emerald-50'

  return (
    <aside className={`flex w-20 flex-col border-r border-[var(--border)] ${bg}`}>
      <nav className="flex flex-1 flex-col items-stretch py-3">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              [
                'flex flex-col items-center justify-center px-2 py-3 text-[11px]',
                isActive
                  ? 'bg-emerald-500 text-slate-950 font-semibold rounded-xl mx-1'
                  : 'hover:bg-emerald-800/80',
              ].join(' ')
            }
          >
            <span className="mb-1 text-base">{item.icon}</span>
            <span className="text-[10px] text-center leading-tight">
              {item.label}
            </span>
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}


