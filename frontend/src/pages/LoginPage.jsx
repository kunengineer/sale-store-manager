import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { signIn } from '../data/services/accountService.js'
import { useToast } from '../layout/Toast.jsx'

export function LoginPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [role, setRole] = useState('ADMIN') // 'ADMIN' | 'EMPLOYEE'
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const toast = useToast()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (!form.username.trim() || !form.password.trim()) {
      setError('Vui lòng nhập đầy đủ thông tin')
      return
    }

    setLoading(true)
    // toast.info('Đang đăng nhập...', 'Vui lòng chờ trong giây lát!')

    try {
      // TODO: truyền role vào nếu BE cần phân biệt endpoint
      const authData = await signIn(form.username, form.password)
      console.log('Data:', authData)

      if (authData.success) {
        const { token, refreshToken, role: userRole } = authData.data

        localStorage.setItem('accessToken', token)
        localStorage.setItem('refreshToken', refreshToken)
        localStorage.setItem('role', userRole)
        if (role === 'ADMIN') {
          // TODO: xử lý luồng ADMIN
          // - check hasStore -> navigate store setup nếu chưa có
          toast.success('Đăng nhập thành công', 'Chào mừng quản trị viên!')
          navigate('/app/admin')
        } else {
          // TODO: xử lý luồng EMPLOYEE
          // - navigate('/app/pos') hoặc trang nhân viên
          toast.success('Đăng nhập thành công', 'Chào mừng nhân viên!')
        }
      }
    } catch (err) {
      if (err.status === 401)      setError('Sai tên đăng nhập hoặc mật khẩu')
      else if (err.status === 403) setError('Tài khoản không có quyền truy cập')
      else if (err.status === 404) setError('Tài khoản không tồn tại')
      else                         setError(err.message ?? 'Có lỗi xảy ra. Vui lòng thử lại sau.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950">
      <div className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/90 p-6 shadow-xl">

        {/* Header */}
        <div className="mb-5 flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-emerald-500 text-sm font-bold text-slate-950">
            POS
          </div>
          <div>
            <p className="text-sm font-semibold text-slate-50">Sale Store Manager</p>
            <p className="text-[11px] text-slate-400">Đăng nhập để tiếp tục</p>
          </div>
        </div>

        {/* Role selector */}
        <div className="mb-4 grid grid-cols-2 gap-2 rounded-xl border border-slate-800 bg-slate-950 p-1">
          <button
            type="button"
            onClick={() => { setRole('ADMIN'); setError('') }}
            className={`rounded-lg py-2 text-[11px] font-semibold transition-all duration-200 flex items-center justify-center gap-1.5
              ${role === 'ADMIN'
                ? 'bg-emerald-500 text-slate-950 shadow'
                : 'text-slate-400 hover:text-slate-200'}`}
          >
            <span>👑</span> Chủ cửa hàng
          </button>
          <button
            type="button"
            onClick={() => { setRole('EMPLOYEE'); setError('') }}
            className={`rounded-lg py-2 text-[11px] font-semibold transition-all duration-200 flex items-center justify-center gap-1.5
              ${role === 'EMPLOYEE'
                ? 'bg-emerald-500 text-slate-950 shadow'
                : 'text-slate-400 hover:text-slate-200'}`}
          >
            <span>🧑‍💼</span> Nhân viên
          </button>
        </div>

        {/* Hint */}
        <p className="mb-4 text-center text-[10px] text-slate-500">
          {role === 'ADMIN'
            ? 'Đăng nhập với tài khoản quản trị để quản lý cửa hàng'
            : 'Đăng nhập với tài khoản được cấp bởi chủ cửa hàng'}
        </p>

        {/* Form */}
        <form className="space-y-3 text-xs" onSubmit={handleSubmit}>
          <div>
            <label className="mb-1 block text-[11px] text-slate-300">Tên đăng nhập</label>
            <input
              value={form.username}
              onChange={(e) => { setForm({ ...form, username: e.target.value }); setError('') }}
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all"
              placeholder={role === 'ADMIN' ? 'admin01' : 'nhanvien01'}
            />
          </div>

          <div>
            <label className="mb-1 block text-[11px] text-slate-300">Mật khẩu</label>
            <input
              type="password"
              value={form.password}
              onChange={(e) => { setForm({ ...form, password: e.target.value }); setError('') }}
              className="w-full rounded-lg border border-slate-700 bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-2 focus:ring-emerald-500 transition-all"
              placeholder="Nhập mật khẩu"
            />
          </div>

          <div className="flex items-center justify-between text-[11px] text-slate-300">
            <label className="flex items-center gap-2 cursor-pointer">
              <input type="checkbox" className="h-3 w-3 rounded border-slate-600 accent-emerald-500" />
              Ghi nhớ đăng nhập
            </label>
            <button type="button" className="text-emerald-400 hover:underline">
              Quên mật khẩu?
            </button>
          </div>

          {error && (
            <p className="rounded-lg border border-rose-800/50 bg-rose-950/30 px-3 py-2 text-center text-[11px] text-rose-400">
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={loading}
            className="mt-1 w-full rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950 disabled:opacity-60 flex items-center justify-center gap-2 transition-opacity"
          >
            {loading && (
              <span className="h-3.5 w-3.5 rounded-full border-2 border-slate-950 border-t-transparent animate-spin" />
            )}
            {loading ? 'Đang đăng nhập...' : role === 'ADMIN' ? '👑 Đăng nhập' : '🧑‍💼 Đăng nhập'}
          </button>
        </form>

        {/* Register link - chỉ hiện với ADMIN */}
        {role === 'ADMIN' && (
          <p className="mt-3 text-center text-[11px] text-slate-400">
            Chưa có tài khoản?{' '}
            <button
              type="button"
              className="cursor-pointer text-emerald-400 hover:underline"
              onClick={() => navigate('/register')}
            >
              Đăng ký tạo cửa hàng
            </button>
          </p>
        )}

        {/* Hint nhân viên */}
        {role === 'EMPLOYEE' && (
          <p className="mt-3 text-center text-[11px] text-slate-500">
            Tài khoản được cấp bởi chủ cửa hàng của bạn
          </p>
        )}
      </div>
    </div>
  )
}