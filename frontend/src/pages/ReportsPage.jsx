export function ReportsPage() {
  const daily = [40, 60, 80, 45, 70, 90, 55]
  const labels = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN']

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">Báo cáo</h1>
          <p className="text-xs text-[var(--muted)]">
            Xem doanh thu theo ngày/tuần/tháng, theo ca, nhân viên, kênh bán.
          </p>
        </div>
        <div className="flex gap-2 text-xs">
          <select className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]">
            <option>Hôm nay</option>
            <option>7 ngày qua</option>
            <option>Tháng này</option>
          </select>
          <button className="rounded-lg border border-[var(--border)] bg-[var(--surface-solid)] px-3 py-1.5 text-xs text-[var(--text)]">
            Xuất báo cáo
          </button>
        </div>
      </div>

      <div className="mb-3 grid gap-3 md:grid-cols-4">
        <div className="rounded-xl border border-emerald-500/40 bg-emerald-500/10 p-3">
          <p className="text-[11px] text-emerald-600">Doanh thu hôm nay</p>
          <p className="mt-1 text-lg font-semibold text-emerald-600">
            12.350.000đ
          </p>
          <p className="mt-1 text-[11px] text-emerald-700">+18% so với hôm qua</p>
        </div>
        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <p className="text-[11px] text-[var(--muted)]">Số hoá đơn</p>
          <p className="mt-1 text-lg font-semibold text-[var(--text)]">124</p>
        </div>
        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <p className="text-[11px] text-[var(--muted)]">Giá trị trung bình</p>
          <p className="mt-1 text-lg font-semibold text-[var(--text)]">99.600đ</p>
        </div>
        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <p className="text-[11px] text-[var(--muted)]">Khách mới</p>
          <p className="mt-1 text-lg font-semibold text-[var(--text)]">23</p>
        </div>
      </div>

      <div className="grid gap-3 md:grid-cols-[1.4fr,1fr]">
        <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
          <p className="mb-2 text-xs font-semibold text-[var(--text)]">
            Doanh thu theo ngày
          </p>
          <div className="flex h-48 flex-col justify-between rounded-lg bg-[var(--surface-2)] px-3 py-2 text-[11px] text-[var(--muted)]">
            <div className="flex flex-1 items-end gap-1">
              {daily.map((h, idx) => (
                // eslint-disable-next-line react/no-array-index-key
                <div
                  key={idx}
                  className="flex-1 rounded-t bg-gradient-to-t from-emerald-500 to-emerald-300"
                  style={{ height: `${h}%` }}
                />
              ))}
            </div>
            <div className="mt-1 flex justify-between">
              {labels.map((l) => (
                <span key={l}>{l}</span>
              ))}
            </div>
          </div>
        </div>

        <div className="space-y-3">
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
            <p className="mb-2 text-xs font-semibold text-[var(--text)]">
              Top sản phẩm
            </p>
            <ul className="space-y-2 text-xs text-[var(--text)]">
              <li className="flex items-center justify-between">
                <span>Cà phê sữa đá</span>
                <span className="text-[11px] text-[var(--muted)]">45 ly</span>
              </li>
              <li className="flex items-center justify-between">
                <span>Trà đào cam sả</span>
                <span className="text-[11px] text-[var(--muted)]">32 ly</span>
              </li>
              <li className="flex items-center justify-between">
                <span>Bánh tiramisu</span>
                <span className="text-[11px] text-[var(--muted)]">18 phần</span>
              </li>
            </ul>
          </div>
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3">
            <p className="mb-2 text-xs font-semibold text-[var(--text)]">
              Doanh thu theo ca
            </p>
            <ul className="space-y-1 text-xs text-[var(--text)]">
              <li className="flex items-center justify-between">
                <span>Ca sáng</span>
                <span className="text-[11px] text-[var(--muted)]">4.200.000đ</span>
              </li>
              <li className="flex items-center justify-between">
                <span>Ca trưa</span>
                <span className="text-[11px] text-[var(--muted)]">3.100.000đ</span>
              </li>
              <li className="flex items-center justify-between">
                <span>Ca tối</span>
                <span className="text-[11px] text-[var(--muted)]">5.050.000đ</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  )
}

