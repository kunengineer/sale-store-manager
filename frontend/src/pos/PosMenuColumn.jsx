import { useState, useMemo } from 'react'
import { usePos } from './PosContext'
import { useStore } from '../store/StoreContext'

export function PosMenuColumn() {
  const { products, addProductToOrder } = usePos()
  const { theme } = useStore()
  const [search, setSearch] = useState('')
  const [activeCategory, setActiveCategory] = useState('Tất cả')
  const categories = ['Tất cả', 'Cafe', 'Trà', 'Bánh', 'Combo']

  const filteredProducts = useMemo(
    () =>
      products.filter((p) => {
        const matchSearch = p.name
          .toLowerCase()
          .includes(search.trim().toLowerCase())
        const matchCategory =
          activeCategory === 'Tất cả' || p.category === activeCategory
        return matchSearch && matchCategory
      }),
    [products, search, activeCategory],
  )

  const outerClass =
    theme === 'dark'
      ? 'border-[var(--border)] bg-[var(--surface)] text-[var(--text)]'
      : 'border-[var(--border)] bg-[var(--surface-solid)] text-[var(--text)]'

  return (
    <section className={`flex flex-1 flex-col rounded-xl border ${outerClass}`}>
      <div className="space-y-2 border-b border-[var(--border)] p-3">
        <input
          className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-sm text-[var(--text)] placeholder:text-[var(--muted)] focus:outline-none focus:ring-2 focus:ring-emerald-500"
          placeholder="Tìm món theo tên hoặc mã (Ctrl + F)..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <div className="flex gap-2 overflow-x-auto">
          {categories.map((cat) => (
            <button
              key={cat}
              type="button"
              onClick={() => setActiveCategory(cat)}
              className={`whitespace-nowrap rounded-full px-3 py-1 text-xs ${
                cat === activeCategory
                  ? 'bg-emerald-500 text-slate-950 font-medium'
                  : 'bg-[var(--surface-2)] text-[var(--text)]'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </div>

      <div className="flex-1 overflow-auto p-3">
        <div className="mb-2 flex items-center text-[11px] text-[var(--muted)]">
          <span className="w-8">STT</span>
          <span className="flex-1">Tên món</span>
          <span className="w-20 text-right">Giá</span>
          <span className="w-14 text-center">Thêm</span>
        </div>
        <div className="space-y-1">
          {filteredProducts.map((p, idx) => (
            <div
              key={p.id}
              className="flex items-center rounded-lg bg-[var(--surface-2)] px-2 py-1.5 text-xs text-[var(--text)] hover:opacity-95"
            >
              <span className="w-8 text-[11px] text-[var(--muted)]">
                {idx + 1}
              </span>
              <div className="flex-1">
                <div className="flex items-center gap-1">
                  <span className="text-sm font-medium">{p.name}</span>
                  {p.tag && (
                    <span className="rounded-full bg-emerald-500/10 px-1.5 py-0.5 text-[9px] font-medium text-emerald-300">
                      {p.tag}
                    </span>
                  )}
                </div>
                <span className="text-[11px] text-[var(--muted)]">
                  {p.category}
                </span>
              </div>
              <span className="w-20 text-right text-sm font-semibold text-emerald-400">
                {p.price.toLocaleString('vi-VN')}đ
              </span>
              <button
                type="button"
                onClick={() => addProductToOrder(p.id)}
                className="ml-2 w-14 rounded-full bg-emerald-500 py-1 text-[11px] font-semibold text-slate-950"
              >
                Thêm
              </button>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

