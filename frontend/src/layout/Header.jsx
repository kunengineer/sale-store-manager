import { useState } from 'react'
import { useStore } from '../store/StoreContext'

export function Header() {
  const {
    stores,
    currentStoreId,
    setCurrentStoreId,
    currentStore,
    theme,
    toggleTheme,
  } = useStore()
  const [menuOpen, setMenuOpen] = useState(false)

  return (
    <header className="flex h-14 items-center justify-between border-b border-[var(--border)] bg-[var(--surface-solid)] px-4">
      <div className="flex items-center gap-2">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-emerald-500 text-xs font-bold text-slate-950">
          POS
        </div>
        <div className="flex flex-col">
          <span className="text-sm font-semibold text-[var(--text)]">
            Sale Store Manager
          </span>
          <span className="text-xs text-[var(--muted)]">Multi-store POS</span>
        </div>
      </div>

      <div className="flex items-center gap-3">
        <div className="flex items-center gap-1 rounded-lg border border-[var(--border)] bg-[var(--surface)] px-2 py-1.5 text-xs text-[var(--text)]">
          <span className="inline-block h-2 w-2 rounded-full bg-emerald-400" />
          <select
             className="bg-transparent text-xs outline-none cursor-pointer
              text-[var(--text)] border border-[var(--border,#334155)] 
              rounded-lg px-2 py-1.5 pr-6
              hover:border-emerald-500/50 transition-colors duration-200
              focus:ring-1 focus:ring-emerald-500 focus:border-emerald-500"
            value={currentStoreId}
            onChange={(e) => setCurrentStoreId(e.target.value)}
          >
            {stores.map((store) => (
              <option
                key={store.id}
                value={store.id}
                className="bg-[var(--surface-solid)] text-[var(--text)]"
              >
                {store.name}
              </option>
            ))}
          </select>
        </div>
        <button
          type="button"
          onClick={toggleTheme}
          className="rounded-full border border-[var(--border)] bg-[var(--surface)] p-1.5 text-xs text-[var(--text)] hover:border-emerald-500"
          aria-label="Toggle dark mode"
        >
          {theme === 'dark' ? '☾' : '☀'}
        </button>

        <div className="relative">
          <button
            type="button"
            onClick={() => setMenuOpen((prev) => !prev)}
            className="flex h-8 w-8 items-center justify-center rounded-full bg-[var(--surface-2)] text-xs font-semibold text-[var(--text)]"
          >
            {currentStore?.name?.[0] ?? 'U'}
          </button>
          {menuOpen && (
            <div className="absolute right-0 mt-2 w-44 rounded-lg border border-[var(--border)] bg-[var(--surface-solid)] py-1 text-xs text-[var(--text)] shadow-xl">
              <div className="px-3 py-1.5 border-b border-[var(--border)] text-[11px]">
                <div className="font-semibold">
                  {currentStore?.name ?? 'Cửa hàng'}
                </div>
                <div className="text-[var(--muted)]">Tài khoản cửa hàng</div>
              </div>
              <button
                type="button"
                className="block w-full px-3 py-1.5 text-left hover:bg-[var(--surface-2)]"
                onClick={() =>
                  window.alert('Thông tin tài khoản sẽ hiển thị ở đây.')
                }
              >
                Thông tin tài khoản
              </button>
              <button
                type="button"
                className="block w-full px-3 py-1.5 text-left hover:bg-[var(--surface-2)]"
                onClick={() => {
                  window.alert('Chức năng này chưa được triển khai. Nhưng vẫn sẽ chuyển đến trang đăng nhập nhé!')
                  window.location.href = '/login'
                }}
              >
                Đăng xuất
              </button>
            </div>
          )}
        </div>
      </div>
    </header>
  )
}

