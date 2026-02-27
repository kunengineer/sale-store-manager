export function RegisterPage() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950">
      <div className="w-full max-w-2xl rounded-2xl border border-slate-800 bg-slate-900/90 p-6 shadow-xl">
        <div className="mb-4 flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-emerald-500 text-sm font-bold text-slate-950">
            POS
          </div>
          <div>
            <p className="text-sm font-semibold text-slate-50">
              Tạo tài khoản &amp; cửa hàng
            </p>
            <p className="text-[11px] text-slate-400">
              Bước 1: Thông tin tài khoản · Bước 2: Thông tin cửa hàng
            </p>
          </div>
        </div>

        <div className="grid gap-4 md:grid-cols-2 text-xs">
          <div className="space-y-3">
            <p className="text-[11px] font-semibold uppercase tracking-wide text-slate-400">
              Bước 1 · Tài khoản
            </p>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Họ và tên
              </label>
              <input
                className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500"
                placeholder="Nguyễn Văn A"
              />
            </div>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Email
              </label>
              <input
                className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500"
                placeholder="banhang@store.vn"
              />
            </div>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Mật khẩu
              </label>
              <input
                type="password"
                className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500"
                placeholder="Tối thiểu 8 ký tự"
              />
            </div>
          </div>

          <div className="space-y-3">
            <p className="text-[11px] font-semibold uppercase tracking-wide text-slate-400">
              Bước 2 · Cửa hàng
            </p>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Tên cửa hàng
              </label>
              <input
                className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500"
                placeholder="Cafe Nhà 89"
              />
            </div>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Loại hình
              </label>
              <select className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100">
                <option>Cafe</option>
                <option>Nhà hàng</option>
                <option>Bán lẻ</option>
              </select>
            </div>
            <div>
              <label className="mb-1 block text-[11px] text-slate-300">
                Địa chỉ
              </label>
              <input
                className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500"
                placeholder="Số nhà, đường, quận/huyện"
              />
            </div>
          </div>
        </div>

        <button className="mt-4 w-full rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950">
          Tạo cửa hàng &amp; vào hệ thống
        </button>
      </div>
    </div>
  )
}

