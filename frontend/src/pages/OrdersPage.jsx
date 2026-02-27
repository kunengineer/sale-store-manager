export function OrdersPage() {
  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">Đơn hàng</h1>
          <p className="text-xs text-[var(--muted)]">
            Danh sách hóa đơn theo ngày, ca làm việc, nhân viên.
          </p>
        </div>
        <button className="rounded-lg border border-[var(--border)] bg-[var(--surface-solid)] px-3 py-1.5 text-xs text-[var(--text)]">
          Xuất file
        </button>
      </div>

      <div className="mb-3 flex flex-wrap gap-2 text-xs">
        <input
          className="w-48 rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-xs text-[var(--text)] placeholder:text-[var(--muted)]"
          placeholder="Tìm theo mã hoá đơn..."
        />
        <select className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]">
          <option>Hôm nay</option>
          <option>7 ngày qua</option>
          <option>Tháng này</option>
        </select>
        <select className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]">
          <option>Tất cả kênh</option>
          <option>Tại quầy</option>
          <option>Online</option>
        </select>
      </div>

      <div className="overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--surface-solid)]">
        <table className="min-w-full text-left text-xs text-[var(--text)]">
          <thead className="bg-[var(--surface)] text-[var(--muted)]">
            <tr>
              <th className="px-3 py-2">Mã hoá đơn</th>
              <th className="px-3 py-2">Thời gian</th>
              <th className="px-3 py-2">Bàn</th>
              <th className="px-3 py-2">Nhân viên</th>
              <th className="px-3 py-2 text-right">Tổng tiền</th>
              <th className="px-3 py-2">Trạng thái</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[var(--border)]">
            <tr>
              <td className="px-3 py-2">HD-000123</td>
              <td className="px-3 py-2">10:23 · Hôm nay</td>
              <td className="px-3 py-2">Bàn A1</td>
              <td className="px-3 py-2">Ngọc D.</td>
              <td className="px-3 py-2 text-right font-semibold text-emerald-500">
                180.000đ
              </td>
              <td className="px-3 py-2">
                <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[11px] text-emerald-600">
                  Đã thanh toán
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  )
}

