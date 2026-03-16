import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

import { useToast } from '../layout/Toast.jsx'
import { registerNewStore } from '../data/services/storeService.js'

// ─── Error codes từ BE ───────────────────────────────────────────────────────
const SERVER_ERROR_CODES = {
  1003: { field: 'username',  message: 'Tên đăng nhập đã tồn tại' },
  1004: { field: 'accEmail',  message: 'Email tài khoản đã được sử dụng' },
}

// ─── Shared UI components ────────────────────────────────────────────────────
function InputField({ label, hint, error, ...props }) {
  return (
    <div>
      <label className="mb-1 block text-[11px] text-slate-300">{label}</label>
      <input
        className={`w-full rounded-lg border bg-slate-950 px-3 py-2 text-xs text-slate-100
          placeholder:text-slate-500 focus:outline-none focus:ring-1 transition-all duration-200
          ${error ? 'border-rose-500 focus:ring-rose-500' : 'border-slate-700 focus:ring-emerald-500'}`}
        {...props}
      />
      {hint && !error && <p className="mt-1 text-[10px] text-slate-500">{hint}</p>}
      {error && <p className="mt-1 text-[10px] text-rose-400">{error}</p>}
    </div>
  )
}

// ─── Step Indicator ──────────────────────────────────────────────────────────
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
            {s === 1 ? 'Tài khoản & chủ sở hữu' : 'Cửa hàng'}
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

// ─── STEP 1 — Tài khoản + Thông tin chủ sở hữu ──────────────────────────────
function Step1({ initialData, onSuccess }) {
  const [form, setForm] = useState(initialData)
  const [errors, setErrors] = useState({})

  const set = (field) => (e) => {
    setForm(f => ({ ...f, [field]: e.target.value }))
    setErrors(er => ({ ...er, [field]: '' }))
  }

  const validate = () => {
    const e = {}

    // ── Thông tin chủ sở hữu ──
    if (!form.fullName.trim())
      e.fullName = 'Vui lòng nhập họ tên'
    if (!form.phone.trim())
      e.phone = 'Vui lòng nhập số điện thoại'
    else if (!/^(0|\+84)[0-9]{9,10}$/.test(form.phone))
      e.phone = 'Số điện thoại không hợp lệ'
    if (!form.empEmail.trim())
      e.empEmail = 'Vui lòng nhập email'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.empEmail))
      e.empEmail = 'Email không hợp lệ'
    if (!form.dob)
      e.dob = 'Vui lòng nhập ngày sinh'

    // ── Thông tin tài khoản ──
    if (!form.username.trim())
      e.username = 'Vui lòng nhập tên đăng nhập'
    else if (form.username.length > 50)
      e.username = 'Tối đa 50 ký tự'
    if (!form.accEmail.trim())
      e.accEmail = 'Vui lòng nhập email tài khoản'
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.accEmail))
      e.accEmail = 'Email không hợp lệ'
    if (form.password.length < 6)
      e.password = 'Mật khẩu tối thiểu 6 ký tự'
    if (form.password !== form.confirmPassword)
      e.confirmPassword = 'Mật khẩu không khớp'

    return e
  }

  const handleNext = () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }
    onSuccess(form)
  }

  return (
    <div className="space-y-4 animate-fadeIn">
      {/* ── Thông tin chủ sở hữu ── */}
      <div>
        <p className="mb-2 text-[10px] font-semibold uppercase tracking-widest text-emerald-500">
          👤 Thông tin chủ sở hữu
        </p>
        <div className="space-y-3">
          <InputField
            label="Họ và tên *" placeholder="Nguyễn Văn A"
            value={form.fullName} onChange={set('fullName')} error={errors.fullName}
          />
          <div className="grid grid-cols-2 gap-3">
            <InputField
              label="Số điện thoại *" placeholder="0912345678"
              value={form.phone} onChange={set('phone')} error={errors.phone}
            />
            <InputField
              label="Ngày sinh *" type="date"
              value={form.dob} onChange={set('dob')} error={errors.dob}
            />
          </div>
          <InputField
            label="Email chủ sở hữu *" type="email" placeholder="owner@example.com"
            value={form.empEmail} onChange={set('empEmail')} error={errors.empEmail}
          />
        </div>
      </div>

      <div className="border-t border-slate-800" />

      {/* ── Thông tin tài khoản ── */}
      <div>
        <p className="mb-2 text-[10px] font-semibold uppercase tracking-widest text-emerald-500">
          🔑 Thông tin tài khoản
        </p>
        <div className="space-y-3">
          <InputField
            label="Tên đăng nhập *" placeholder="admin01"
            value={form.username} onChange={set('username')} error={errors.username}
          />
          <InputField
            label="Email tài khoản *" type="email" placeholder="login@example.com"
            hint="Email dùng để đăng nhập hệ thống"
            value={form.accEmail} onChange={set('accEmail')} error={errors.accEmail}
          />
          <InputField
            label="Mật khẩu *" type="password" placeholder="Tối thiểu 6 ký tự"
            value={form.password} onChange={set('password')} error={errors.password}
          />
          <InputField
            label="Xác nhận mật khẩu *" type="password" placeholder="Nhập lại mật khẩu"
            value={form.confirmPassword} onChange={set('confirmPassword')} error={errors.confirmPassword}
          />
        </div>
      </div>

      <button
        onClick={handleNext}
        className="mt-2 w-full rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950
          flex items-center justify-center gap-2 hover:bg-emerald-400 transition-colors"
      >
        Tiếp theo → Thông tin cửa hàng
      </button>
    </div>
  )
}

// ─── STEP 2 — Thông tin cửa hàng ────────────────────────────────────────────
function Step2({ step1Data, onBack, onDone }) {
  const toast = useToast()
  const [loading, setLoading] = useState(false)
  const [form, setForm] = useState({
    storeCode: '', storeName: '', address: '',
    storeEmail: '', openTime: '08:00', closeTime: '22:00',
  })
  const [errors, setErrors] = useState({})

  const set = (field) => (e) => {
    setForm(f => ({ ...f, [field]: e.target.value }))
    setErrors(er => ({ ...er, [field]: '' }))
  }

  const validate = () => {
    const e = {}
    if (!form.storeCode.trim())
      e.storeCode = 'Vui lòng nhập mã cửa hàng'
    else if (form.storeCode.length > 20)
      e.storeCode = 'Tối đa 20 ký tự'
    if (!form.storeName.trim())
      e.storeName = 'Vui lòng nhập tên cửa hàng'
    if (!form.address.trim())
      e.address = 'Vui lòng nhập địa chỉ'
    if (form.storeEmail && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.storeEmail))
      e.storeEmail = 'Email không hợp lệ'
    return e
  }

  // Đóng gói toàn bộ data thành RegisterNewStore và gọi 1 API duy nhất
  const buildPayload = () => ({
    storeCode: form.storeCode,
    storeName: form.storeName,
    address:   form.address,
    email:     form.storeEmail || undefined,
    openTime:  form.openTime + ':00',   // LocalTime: "HH:mm:ss"
    closeTime: form.closeTime + ':00',
    employee: {
      fullName: step1Data.fullName,
      phone:    step1Data.phone,
      email:    step1Data.empEmail,
      dob:      step1Data.dob ? step1Data.dob + 'T00:00:00' : undefined, // LocalDateTime
      accountCreateRequest: {
        username: step1Data.username,
        password: step1Data.password,
        email:    step1Data.accEmail,
      },
    },
  })

  const handleSubmit = async () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }

    setLoading(true)
    try {
      await registerNewStore(buildPayload())
      toast.success('Đăng ký thành công!', 'Chào mừng bạn đến với hệ thống!')
      onDone()
    } catch (err) {
      console.error('Register error:', err)
      // Map lỗi server trả về field tương ứng
      const fieldErrors = {}
      ;(err.errors ?? []).forEach(({ code }) => {
        const mapped = SERVER_ERROR_CODES[code]
        if (mapped) fieldErrors[mapped.field] = mapped.message
      })
      if (Object.keys(fieldErrors).length) {
        // Có lỗi ở step 1 → quay lại và hiển thị lỗi (thông báo user)
        toast.error(
          'Thông tin tài khoản bị trùng!',
          'Vui lòng quay lại bước 1 và kiểm tra lại tên đăng nhập hoặc email.'
        )
      } else {
        toast.error('Có lỗi xảy ra!', err.message ?? 'Không thể tạo cửa hàng, vui lòng thử lại.')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-3 animate-fadeIn">
      <div className="grid grid-cols-2 gap-3">
        <InputField
          label="Mã cửa hàng *" placeholder="STR001"
          value={form.storeCode} onChange={set('storeCode')} error={errors.storeCode}
        />
        <InputField
          label="Email cửa hàng" type="email" placeholder="store@example.vn"
          value={form.storeEmail} onChange={set('storeEmail')} error={errors.storeEmail}
        />
      </div>
      <InputField
        label="Tên cửa hàng *" placeholder="Highland Coffee Nguyễn Trãi"
        value={form.storeName} onChange={set('storeName')} error={errors.storeName}
      />
      <InputField
        label="Địa chỉ *" placeholder="123 Nguyễn Trãi, Quận 1, TP.HCM"
        value={form.address} onChange={set('address')} error={errors.address}
      />
      <div className="grid grid-cols-2 gap-3">
        <InputField
          label="Giờ mở cửa" type="time"
          value={form.openTime} onChange={set('openTime')}
        />
        <InputField
          label="Giờ đóng cửa" type="time"
          value={form.closeTime} onChange={set('closeTime')}
        />
      </div>

      {/* Tóm tắt step 1 */}
      <div className="rounded-lg border border-slate-700/60 bg-slate-800/40 px-3 py-2 text-[10px] text-slate-400 space-y-0.5">
        <p className="text-slate-300 font-semibold mb-1">✅ Đã xác nhận ở bước 1</p>
        <p>👤 Chủ sở hữu: <span className="text-slate-200">{step1Data.fullName}</span></p>
        <p>🔑 Tài khoản: <span className="text-slate-200">{step1Data.username}</span></p>
      </div>

      <div className="flex gap-2 mt-2">
        <button
          onClick={onBack}
          disabled={loading}
          className="rounded-lg border border-slate-700 px-4 py-2 text-xs font-semibold text-slate-300
            hover:border-slate-500 transition-colors disabled:opacity-40"
        >
          ← Quay lại
        </button>
        <button
          onClick={handleSubmit}
          disabled={loading}
          className="flex-1 rounded-lg bg-emerald-500 py-2 text-xs font-semibold text-slate-950
            disabled:opacity-60 flex items-center justify-center gap-2 hover:bg-emerald-400 transition-colors"
        >
          {loading && (
            <span className="h-3.5 w-3.5 rounded-full border-2 border-slate-950 border-t-transparent animate-spin" />
          )}
          {loading ? 'Đang tạo cửa hàng...' : '🎉 Hoàn tất & vào hệ thống'}
        </button>
      </div>
    </div>
  )
}

// ─── MAIN ────────────────────────────────────────────────────────────────────
export function RegisterPage() {
  const navigate  = useNavigate()
  const [step, setStep] = useState(1)

  // Data từ step 1 được giữ ở đây để truyền xuống step 2 khi submit
  const [step1Data, setStep1Data] = useState({
    // Employee
    fullName: '', phone: '', empEmail: '', dob: '',
    // Account
    username: '', accEmail: '', password: '', confirmPassword: '',
  })

  const goToStep2 = (data) => {
    setStep1Data(data)
    setStep(2)
  }

  const goBack = () => setStep(1)

  const handleDone = () => navigate('/login')

  return (
    <div className="flex min-h-screen items-center justify-center bg-slate-950 px-4">
      <style>{`
        @keyframes fadeInRight {
          from { opacity: 0; transform: translateX(24px); }
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
            <p className="text-sm font-semibold text-slate-50">Đăng ký cửa hàng</p>
            <p className="text-[11px] text-slate-400">Đăng ký để bắt đầu quản lý cửa hàng</p>
          </div>
        </div>

        <StepIndicator step={step} />

        <p className="mb-3 text-[11px] font-semibold uppercase tracking-wide text-slate-400">
          {step === 1 ? 'Bước 1 · Tài khoản & chủ sở hữu' : 'Bước 2 · Thông tin cửa hàng'}
        </p>

        <div key={step}>
          {step === 1
            ? <Step1 initialData={step1Data} onSuccess={goToStep2} />
            : <Step2 step1Data={step1Data} onBack={goBack} onDone={handleDone} />
          }
        </div>

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