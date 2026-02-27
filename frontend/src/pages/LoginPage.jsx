import { useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { signIn } from '../data/services/accountService.js'
import { useToast } from '../layout/Toast.jsx'

export function LoginPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ username: '', password: '' })
  const [error, setError] = useState('')
   const toast = useToast()

  const handleSubmit = async (e) => {
    e.preventDefault()
    toast.info('Đang đăng nhập...', 'Vui lòng chờ trong giây lát!')
    console.log('Submitting login form with:', form)
    try {
      const authData = await signIn(form.username, form.password)
      // authData = { accessToken, accountId, fullName, userName, role }

      console.log('Data:', authData)
      console.log('Role:', authData.data.role)

      // TODO: lưu vào store/context sau
      //localStorage.setItem('token', authData.accessToken)
      if(authData.data.success) {
        if (authData.data.role === 'ADMIN') {
          toast.success('Dăng nhập thành công','Chào mừng bạn đến với Sale Store Manager!')
          //navigate('/app/admin')
        } else if (authData.data.role === 'EMPLOYEE') {
          toast.success('Đăng nhập thành công với vai trò nhân viên! Nhưng chức năng này chưa được triển khai nên sẽ chuyển đến trang bán hàng nhé!')
          //navigate('/app/admin') // tạm thời vẫn cho admin vào, sau này sẽ có trang riêng cho nhân viên bán hàng
        }
    }

    } catch (err) {
      if (err.status === 401) {
        setError('Sai email/số điện thoại hoặc mật khẩu')
      } else if (err.status === 403) {
        setError('Tài khoản của bạn không có quyền truy cập')
      }
      else if(err.status === 404){
        setError('Tài khoản không tồn tại')
      }
      else {
        setError('Có lỗi xảy ra. Vui lòng thử lại sau.')
      }
      setError(err.message)
    }
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
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
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
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
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

          {error && (
              <p className="text-center text-[11px] text-red-400">{error}</p>
            )}

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

