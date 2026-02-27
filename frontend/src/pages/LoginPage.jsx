import { useNavigate } from 'react-router-dom'

export function LoginPage() {
  const navigate = useNavigate()

  const handleSubmit = (event) => {
    event.preventDefault()
    // Tạm thời: cho vào dashboard để thao tác UI
    navigate('/app/dashboard')
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950">
      <div className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/90 p-6 shadow-xl">
        <div className="mb-4 flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-emerald-500 text-sm font-bold text-slate-950">
            POS
          </div>
          <div>
            <p className="text-sm font-semibold text-slate-50">
              Sale Store Manager
            </p>
            <p className="text-[11px] text-slate-400">
              Đăng nhập để tiếp tục
            </p>
          </div>
        </div>

        <form className="space-y-3 text-xs" onSubmit={handleSubmit}>
          <div>
            <label className="mb-1 block text-[11px] text-slate-300">
              Email hoặc số điện thoại
            </label>
            <input
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              placeholder="vd: banhang@store.vn"
            />
          </div>
          <div>
            <label className="mb-1 block text-[11px] text-slate-300">
              Mật khẩu
            </label>
            <input
              type="password"
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              placeholder="Nhập mật khẩu"
            />
          </div>

          <div className="flex items-center justify-between text-[11px] text-slate-300">
            <label className="flex items-center gap-2">
              <input type="checkbox" className="h-3 w-3 rounded border-slate-600" />
              Ghi nhớ đăng nhập
            </label>
            <button type="button" className="text-emerald-400">
              Quên mật khẩu?
            </button>
          </div>

          <button
            type="submit"
            className="mt-1 w-full rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950"
          >
            Đăng nhập
          </button>
        </form>

        <p className="mt-3 text-center text-[11px] text-slate-400">
          Chưa có tài khoản?{' '}
          <button
            type="button"
            className="cursor-pointer text-emerald-400"
            onClick={() => navigate('/register')}
          >
            Đăng ký tạo cửa hàng
          </button>
        </p>
      </div>
    </div>
  )
}

