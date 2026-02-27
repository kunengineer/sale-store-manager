export function DashboardPage() {
  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-slate-50">Tổng quan</h1>
          <p className="text-xs text-slate-400">
            Ảnh tổng thể hoạt động của cửa hàng trong ngày.
          </p>
        </div>
        <select className="rounded-lg border border-slate-700 bg-slate-950 px-2 py-2 text-xs text-slate-100">
          <option>Hôm nay</option>
          <option>7 ngày qua</option>
          <option>Tháng này</option>
        </select>
      </div>

      <div className="mb-3 grid gap-3 md:grid-cols-4">
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="text-[11px] text-slate-400">Doanh thu hôm nay</p>
          <p className="mt-1 text-lg font-semibold text-emerald-400">
            12.350.000đ
          </p>
        </div>
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="text-[11px] text-slate-400">Số hoá đơn</p>
          <p className="mt-1 text-lg font-semibold text-slate-50">124</p>
        </div>
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="text-[11px] text-slate-400">Bàn đang phục vụ</p>
          <p className="mt-1 text-lg font-semibold text-slate-50">9</p>
        </div>
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="text-[11px] text-slate-400">Khách trong quán</p>
          <p className="mt-1 text-lg font-semibold text-slate-50">32</p>
        </div>
      </div>

      <div className="grid gap-3 md:grid-cols-[1.4fr,1fr]">
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="mb-2 text-xs font-semibold text-slate-200">
            Lưu lượng khách theo khung giờ
          </p>
          <div className="flex h-48 items-end gap-1 rounded-lg bg-slate-950 px-3 py-2">
            {[20, 35, 50, 80, 90, 60, 40].map((h, idx) => (
              // eslint-disable-next-line react/no-array-index-key
              <div
                key={idx}
                className="flex-1 rounded-t bg-emerald-500/70"
                style={{ height: `${h}%` }}
              />
            ))}
          </div>
        </div>
        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3">
          <p className="mb-2 text-xs font-semibold text-slate-200">
            Hiệu suất theo ca
          </p>
          <ul className="space-y-2 text-xs text-slate-200">
            <li className="flex items-center justify-between">
              <span>Ca sáng</span>
              <span className="text-[11px] text-slate-400">4.200.000đ</span>
            </li>
            <li className="flex items-center justify-between">
              <span>Ca trưa</span>
              <span className="text-[11px] text-slate-400">3.100.000đ</span>
            </li>
            <li className="flex items-center justify-between">
              <span>Ca tối</span>
              <span className="text-[11px] text-slate-400">5.050.000đ</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
  )
}

