import { useStore } from '../store/StoreContext'

export function SettingsPage() {
  const { settings, setSettings } = useStore()

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3">
        <h1 className="text-lg font-semibold text-slate-50">Cài đặt</h1>
        <p className="text-xs text-slate-400">
          Cấu hình cửa hàng, thuế, phí dịch vụ, hoá đơn và giao diện POS.
        </p>
      </div>

      <div className="grid gap-3 md:grid-cols-[0.9fr,1.6fr]">
        <div className="space-y-2 text-xs">
          <button className="flex w-full justify-between rounded-lg bg-slate-900 px-3 py-2 text-left text-slate-100">
            <span>Thông tin cửa hàng</span>
          </button>
          <button className="flex w-full justify-between rounded-lg bg-slate-900 px-3 py-2 text-left text-slate-100">
            <span>Thuế &amp; phí dịch vụ</span>
          </button>
          <button className="flex w-full justify-between rounded-lg bg-slate-900 px-3 py-2 text-left text-slate-100">
            <span>Cấu hình in hoá đơn</span>
          </button>
          <button className="flex w-full justify-between rounded-lg bg-slate-900 px-3 py-2 text-left text-slate-100">
            <span>Giao diện POS</span>
          </button>
        </div>

        <div className="rounded-xl border border-slate-800 bg-slate-900 p-3 text-xs text-slate-200">
          <h2 className="mb-2 text-sm font-semibold text-slate-50">
            Giao diện POS
          </h2>
          <div className="space-y-3">
            <label className="block">
              <span className="text-[11px] text-slate-400">
                Chủ đề màu sắc
              </span>
              <select className="mt-1 w-64 rounded-lg border border-slate-700 bg-slate-950 px-2 py-2 text-xs text-slate-100">
                <option>Xanh ngọc (mặc định)</option>
                <option>Xanh dương</option>
                <option>Tím</option>
              </select>
            </label>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                className="h-3 w-3 rounded border-slate-600"
                checked={settings.autoDarkModeOnPos}
                onChange={(e) =>
                  setSettings((prev) => ({
                    ...prev,
                    autoDarkModeOnPos: e.target.checked,
                  }))
                }
              />
              <span className="text-xs">
                Tự động bật dark mode trên màn POS
              </span>
            </label>
            <label className="flex items-center gap-2">
              <input
                type="checkbox"
                className="h-3 w-3 rounded border-slate-600"
                checked={settings.hideSidebarInPos}
                onChange={(e) =>
                  setSettings((prev) => ({
                    ...prev,
                    hideSidebarInPos: e.target.checked,
                  }))
                }
              />
              <span className="text-xs">
                Ẩn sidebar khi vào màn hình POS để tối đa không gian
              </span>
            </label>
          </div>
        </div>
      </div>
    </div>
  )
}

