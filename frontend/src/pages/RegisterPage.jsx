import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

// ── import toast & services của bạn ──────────────────────────
import { useToast } from '../layout/Toast.jsx'
import { createAccount } from '../data/services/accountService.js'
import { createStore } from '../data/services/storeService.js'

// ─────────────────────────────────────────────────────────────

const ERROR_CODES = {
  1003: { field: 'username', message: 'Tên đăng nhập đã tồn tại' },
  1004: { field: 'email',    message: 'Email đã được sử dụng' },
}

const ROLES = [
  { value: 'ADMIN',    label: 'Chủ cửa hàng' },
  { value: 'MANAGER',  label: 'Quản lý' },
]

const STORE_TYPES = ['Cafe', 'Nhà hàng', 'Bán lẻ', 'Tiệm tạp hoá', 'Khác']

function InputField({ label, error, ...props }) {
  return (
    <div>
      <label className="mb-1 block text-[11px] text-slate-300">{label}</label>
      <input
        className={`w-full rounded-lg border bg-slate-950 px-3 py-2 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:ring-1 transition-all duration-200
          ${error ? 'border-rose-500 focus:ring-rose-500' : 'border-slate-700 focus:ring-emerald-500'}`}
        {...props}
      />
      {error && <p className="mt-1 text-[10px] text-rose-400">{error}</p>}
    </div>
  )
}

function SelectField({ label, error, children, ...props }) {
  return (
    <div>
      <label className="mb-1 block text-[11px] text-slate-300">{label}</label>
      <select
        className={`w-full rounded-lg border bg-slate-950 px-3 py-2 text-xs text-slate-100 focus:outline-none focus:ring-1 transition-all duration-200
          ${error ? 'border-rose-500 focus:ring-rose-500' : 'border-slate-700 focus:ring-emerald-500'}`}
        {...props}
      >
        {children}
      </select>
      {error && <p className="mt-1 text-[10px] text-rose-400">{error}</p>}
    </div>
  )
}

// ── STEP INDICATOR ────────────────────────────────────────────
function StepIndicator({ step }) {
  return (
    <div className="mb-5 flex items-center gap-2">
      {[1, 2].map((s) => (
        <div key={s} className="flex items-center gap-2">
          <div className={`flex h-6 w-6 items-center justify-center rounded-full text-[10px] font-bold transition-all duration-500
            ${step >= s ? 'bg-emerald-500 text-slate-950' : 'bg-slate-800 text-slate-500'}`}>
            {step > s ? '✓' : s}
          </div>
          <span className={`text-[11px] transition-colors duration-300
            ${step >= s ? 'text-slate-200' : 'text-slate-500'}`}>
            {s === 1 ? 'Tài khoản' : 'Cửa hàng'}
          </span>
          {s < 2 && (
            <div className="mx-1 h-px w-8 bg-slate-700 relative overflow-hidden rounded">
              <div className={`absolute inset-y-0 left-0 bg-emerald-500 transition-all duration-700 ${step > 1 ? 'w-full' : 'w-0'}`} />
            </div>
          )}
        </div>
      ))}
    </div>
  )
}

// ── STEP 1: TÀI KHOẢN ─────────────────────────────────────────
function Step1({ onSuccess }) {
  const toast = useToast()
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({
    fullName: '', username: '', email: '', password: '', confirmPassword: '', role: 'ADMIN',
  })
  const [errors, setErrors] = useState({})

  const set = (field) => (e) => {
    setForm(f => ({ ...f, [field]: e.target.value }))
    setErrors(er => ({ ...er, [field]: '' }))
  }

  const validate = () => {
    const e = {}
    if (!form.fullName.trim())        e.fullName = 'Vui lòng nhập họ tên'
    if (!form.username.trim())        e.username = 'Vui lòng nhập tên đăng nhập'
    if (form.username.length > 50)    e.username = 'Tối đa 50 ký tự'
    if (!form.email.trim())           e.email = 'Vui lòng nhập email'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) e.email = 'Email không hợp lệ'
    if (form.password.length < 6)     e.password = 'Mật khẩu tối thiểu 6 ký tự'
    if (form.password !== form.confirmPassword) e.confirmPassword = 'Mật khẩu không khớp'
    return e
  }

  const handleSubmit = async () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }

    setLoading(true)
    try {
      const res = await createAccount({
          fullName: form.fullName,
          username: form.username,
          email:    form.email,
          password: form.password,
          role:     form.role,
        })

        console.log('Account created:', res)  // interceptor unwrap APIResponse → res = AccountResponse trực tiếp
      // interceptor unwrap APIResponse → res = AccountResponse trực tiếp
      // res = { accountId, fullName, userName, email, createdAt, role }
      toast.success('Tạo tài khoản thành công!', `Chào ${res.data.fullName} `)
      onSuccess(res.data.accountId)
      localStorage.setItem('accountId', res.data.accountId)  // ← tạm lưu accountId để dùng bước 2, sau này có thể xoá

    } catch (err) {
      // err = { status, message, errors } — do interceptor format
      const fieldErrors = {}
      ;(err.errors ?? []).forEach(({ code }) => {
        const mapped = ERROR_CODES[code]
        if (mapped) fieldErrors[mapped.field] = mapped.message
      })
      if (Object.keys(fieldErrors).length) setErrors(fieldErrors)
      else toast.error('Có lỗi xảy ra!', err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-3 animate-fadeIn">
      <InputField label="Họ và tên *" placeholder="Nguyễn Văn A"
        value={form.fullName} onChange={set('fullName')} error={errors.fullName} />
      <InputField label="Tên đăng nhập *" placeholder="admin01"
        value={form.username} onChange={set('username')} error={errors.username} />
      <InputField label="Email *" type="email" placeholder="banhang@store.vn"
        value={form.email} onChange={set('email')} error={errors.email} />
      <SelectField label="Vai trò" value={form.role} onChange={set('role')} error={errors.role}>
        {ROLES.map(r => <option key={r.value} value={r.value}>{r.label}</option>)}
      </SelectField>
      <InputField label="Mật khẩu *" type="password" placeholder="Tối thiểu 6 ký tự"
        value={form.password} onChange={set('password')} error={errors.password} />
      <InputField label="Xác nhận mật khẩu *" type="password" placeholder="Nhập lại mật khẩu"
        value={form.confirmPassword} onChange={set('confirmPassword')} error={errors.confirmPassword} />

      <button
        onClick={handleSubmit}
        disabled={loading}
        className="mt-2 w-full rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950 disabled:opacity-60 flex items-center justify-center gap-2 transition-opacity"
      >
        {loading && <span className="h-3.5 w-3.5 rounded-full border-2 border-slate-950 border-t-transparent animate-spin" />}
        {loading ? 'Đang tạo tài khoản...' : 'Tiếp theo → Tạo cửa hàng'}
      </button>
    </div>
  )
}

// ── STEP 2: CỬA HÀNG ──────────────────────────────────────────
function Step2({ accountId, onBack, onDone }) {
  const toast = useToast()
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({
    storeCode: '', storeName: '', address: '', phone: '',
    email: '', openTime: '08:00', closeTime: '22:00', storeType: 'Cafe',
  })
  const [errors, setErrors] = useState({})

  const set = (field) => (e) => {
    setForm(f => ({ ...f, [field]: e.target.value }))
    setErrors(er => ({ ...er, [field]: '' }))
  }

  const validate = () => {
    const e = {}
    if (!form.storeCode.trim())  e.storeCode  = 'Vui lòng nhập mã cửa hàng'
    if (form.storeCode.length > 20) e.storeCode = 'Tối đa 20 ký tự'
    if (!form.storeName.trim())  e.storeName  = 'Vui lòng nhập tên cửa hàng'
    if (!form.address.trim())    e.address    = 'Vui lòng nhập địa chỉ'
    if (form.phone && !/^(0|\+84)[0-9]{9,10}$/.test(form.phone))
      e.phone = 'Số điện thoại không hợp lệ'
    if (form.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      e.email = 'Email không hợp lệ'
    return e
  }

  const handleSubmit = async () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }

    setLoading(true)
    const accountId = localStorage.getItem('accountId')  // ← lấy accountId đã lưu tạm ở bước 1
    try {
      const res = await createStore({
        storeCode: form.storeCode,
        storeName: form.storeName,
        address:   form.address,
        phone:     form.phone   || undefined,
        email:     form.email   || undefined,
        openTime:  form.openTime,
        closeTime: form.closeTime,
        accountId,
      })
      toast.success('Tạo cửa hàng thành công!', 'Chào mừng bạn đến với hệ thống!')
      localStorage.removeItem('accountId') 
      onDone()
    } catch (err) {
      toast.error('Có lỗi xảy ra!', 'Không thể tạo cửa hàng, vui lòng thử lại')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-3 animate-fadeIn">
      <div className="grid grid-cols-2 gap-3">
        <InputField label="Mã cửa hàng *" placeholder="STR001"
          value={form.storeCode} onChange={set('storeCode')} error={errors.storeCode} />
        <SelectField label="Loại hình" value={form.storeType} onChange={set('storeType')}>
          {STORE_TYPES.map(t => <option key={t}>{t}</option>)}
        </SelectField>
      </div>
      <InputField label="Tên cửa hàng *" placeholder="Highland Coffee Nguyễn Trãi"
        value={form.storeName} onChange={set('storeName')} error={errors.storeName} />
      <InputField label="Địa chỉ *" placeholder="123 Nguyễn Trãi, Quận 1, TP.HCM"
        value={form.address} onChange={set('address')} error={errors.address} />
      <div className="grid grid-cols-2 gap-3">
        <InputField label="Số điện thoại" placeholder="0912345678"
          value={form.phone} onChange={set('phone')} error={errors.phone} />
        <InputField label="Email cửa hàng" type="email" placeholder="store@example.vn"
          value={form.email} onChange={set('email')} error={errors.email} />
      </div>
      <div className="grid grid-cols-2 gap-3">
        <InputField label="Giờ mở cửa" type="time"
          value={form.openTime} onChange={set('openTime')} />
        <InputField label="Giờ đóng cửa" type="time"
          value={form.closeTime} onChange={set('closeTime')} />
      </div>

      <div className="flex gap-2 mt-2">
        <button
          onClick={onBack}
          disabled={loading}
          className="rounded-lg border border-slate-700 px-4 py-2 text-xs font-semibold text-slate-300 hover:border-slate-500 transition-colors disabled:opacity-40"
        >
          ← Quay lại
        </button>
        <button
          onClick={handleSubmit}
          disabled={loading}
          className="flex-1 rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950 disabled:opacity-60 flex items-center justify-center gap-2 transition-opacity"
        >
          {loading && <span className="h-3.5 w-3.5 rounded-full border-2 border-slate-950 border-t-transparent animate-spin" />}
          {loading ? 'Đang tạo cửa hàng...' : '🎉 Tạo cửa hàng & vào hệ thống'}
        </button>
      </div>
    </div>
  )
}

// ── MAIN COMPONENT ────────────────────────────────────────────
export function RegisterPage() {
  const navigate = useNavigate()
  const [step, setStep] = useState(1)
  const [accountId, setAccountId] = useState(null)
  const [slideDir, setSlideDir] = useState('right')

  const goToStep2 = (id) => {
    setSlideDir('right')
    setAccountId(id)
    setTimeout(() => setStep(2), 50)
  }

  const goBack = () => {
    setSlideDir('left')
    setTimeout(() => setStep(1), 50)
  }

  const handleDone = () => {
    navigate('/login')
  }

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950 px-4">
      <style>{`
        @keyframes fadeInRight {
          from { opacity: 0; transform: translateX(24px); }
          to   { opacity: 1; transform: translateX(0); }
        }
        @keyframes fadeInLeft {
          from { opacity: 0; transform: translateX(-24px); }
          to   { opacity: 1; transform: translateX(0); }
        }
        .animate-fadeIn {
          animation: fadeInRight 0.35s cubic-bezier(0.34,1.2,0.64,1) both;
        }
      `}</style>

      <div className="w-full max-w-md rounded-2xl border border-slate-800 bg-slate-900/90 p-6 shadow-xl">
        {/* Header */}
        <div className="mb-4 flex items-center gap-2">
          <div className="flex h-9 w-9 items-center justify-center rounded-xl bg-emerald-500 text-sm font-bold text-slate-950">
            POS
          </div>
          <div>
            <p className="text-sm font-semibold text-slate-50">
              Tạo tài khoản &amp; cửa hàng
            </p>
            <p className="text-[11px] text-slate-400">
              Đăng ký để bắt đầu quản lý cửa hàng
            </p>
          </div>
        </div>

        <StepIndicator step={step} />

        {/* Step title */}
        <p className="mb-3 text-[11px] font-semibold uppercase tracking-wide text-slate-400">
          {step === 1 ? 'Bước 1 · Thông tin tài khoản' : 'Bước 2 · Thông tin cửa hàng'}
        </p>

        {/* Note lỗ hổng - nhắc user */}
        {step === 2 && (
          <div className="mb-3 rounded-lg border border-amber-800/50 bg-amber-950/30 px-3 py-2 text-[10px] text-amber-400">
            ⚠ Tài khoản chỉ hoạt động sau khi hoàn tất tạo cửa hàng. Vui lòng không thoát trang.
          </div>
        )}

        {/* Steps */}
        <div key={step}>
          {step === 1
            ? <Step1 onSuccess={goToStep2} />
            : <Step2 accountId={accountId} onBack={goBack} onDone={handleDone} />
          }
        </div>

        {/* Login link */}
        {step === 1 && (
          <p className="mt-3 text-center text-[11px] text-slate-400">
            Đã có tài khoản?{' '}
            <button
              onClick={() => navigate('/login')}
              className="text-emerald-400 hover:underline"
            >
              Đăng nhập
            </button>
          </p>
        )}
      </div>
    </div>
  )
}