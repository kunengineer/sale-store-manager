import { useState, useMemo } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useStore } from '../store/StoreContext'
import { getProducts, createProduct, updateProduct } from '../data/services/productService'
import {
  getProductVariantsByProductId,
  createProductVariant,
  updateProductVariant,
} from '../data/services/productVariantService'
import { getCategories } from '../data/services/categoryService'

// ─────────────────────────────────────────────────────────────
// CONSTANTS
// ─────────────────────────────────────────────────────────────
const VARIANT_TYPES = [
  { value: 'BASE',    label: 'Kích cỡ' },
  { value: 'TOPPING', label: 'Topping' },
  // { value: 'OPTION',  label: 'Tùy chọn' },
  // { value: 'OTHER',   label: 'Khác' },
]

const VARIANT_TYPE_LABEL = Object.fromEntries(VARIANT_TYPES.map(v => [v.value, v.label]))

const EMPTY_PRODUCT_FORM = {
  productCode: '',
  productName: '',
  description: '',
  unit: '',
  images: '',
  isActive: true,
}

const EMPTY_VARIANT_FORM = {
  variantName: '',
  variantType: 'SIZE',
  sku: '',
  barcode: '',
  attributes: '',
  price: '',
  costPrice: '',
  weightGram: '',
  isActive: true,
}

const EMPTY_CREATE_FORM = {
  categoryId: '',
  productCode: '',
  productName: '',
  description: '',
  unit: '',
  basePrice: '',
  costPrice: '',
  images: '',
  isActive: true,
}

// ─────────────────────────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────────────────────────
const fmtMoney  = (n) => (n != null ? Number(n).toLocaleString('vi-VN') + 'đ' : '—')
const fmtDate   = (iso) => {
  if (!iso) return '—'
  const d = new Date(iso)
  return d.toLocaleDateString('vi-VN') + ' · ' + d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
}
// Lấy chữ cái đầu của tên sản phẩm
const getInitials = (name = '') =>
  name.trim().split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase() || '?'

// ─────────────────────────────────────────────────────────────
// SHARED UI COMPONENTS
// ─────────────────────────────────────────────────────────────

/** Label + input wrapper với error message */
function Field({ label, error, children }) {
  return (
    <div>
      <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">{label}</label>
      {children}
      {error && <p className="mt-0.5 text-[10px] font-semibold text-rose-500">{error}</p>}
    </div>
  )
}

/** Input chuẩn — theo theme */
function Inp({ error, ...props }) {
  return (
    <input
      className={`w-full rounded-lg border px-2.5 py-1.5 text-xs text-[var(--text)] bg-[var(--input)]
        focus:outline-none focus:ring-2 transition-all
        ${error
          ? 'border-rose-400 focus:ring-rose-400/30'
          : 'border-[var(--border)] focus:ring-emerald-500/30 focus:border-emerald-500'}`}
      {...props}
    />
  )
}

/** Select chuẩn — theo theme */
function Sel({ children, ...props }) {
  return (
    <select
      className="w-full rounded-lg border border-[var(--border)] px-2.5 py-1.5 text-xs
        text-[var(--text)] bg-[var(--input)] focus:outline-none focus:ring-2
        focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
      {...props}
    >{children}</select>
  )
}

/** Textarea chuẩn */
function Textarea({ ...props }) {
  return (
    <textarea
      rows={2}
      className="w-full resize-none rounded-lg border border-[var(--border)] bg-[var(--input)]
        px-2.5 py-1.5 text-xs text-[var(--text)] focus:outline-none
        focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
      {...props}
    />
  )
}

/** Modal overlay wrapper — dùng chung cho product & variant form */
function Modal({ title, onClose, onSubmit, loading, children, wide = false }) {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 px-4 py-6 backdrop-blur-sm">
      <div className={`w-full ${wide ? 'max-w-2xl' : 'max-w-md'} rounded-2xl border border-[var(--border)]
        bg-[var(--surface-solid)] p-5 shadow-2xl max-h-[90vh] flex flex-col`}>
        {/* Header */}
        <div className="mb-4 flex items-center justify-between shrink-0">
          <h3 className="text-sm font-bold text-[var(--text)]">{title}</h3>
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
              text-[var(--muted)] hover:border-rose-400 hover:text-rose-400 transition-all">
            ✕
          </button>
        </div>

        {/* Scrollable body */}
        <div className="flex-1 overflow-auto space-y-3 pr-1">{children}</div>

        {/* Footer actions */}
        <div className="mt-5 flex justify-end gap-2 shrink-0">
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-4 py-1.5 text-xs
              text-[var(--text)] hover:bg-[var(--surface-2)] transition-colors">
            Hủy
          </button>
          <button type="button" onClick={onSubmit} disabled={loading}
            className="flex items-center gap-1.5 rounded-lg bg-emerald-500 px-5 py-1.5
              text-xs font-bold text-white disabled:opacity-60 hover:bg-emerald-600 transition-colors">
            {loading && (
              <span className="h-3 w-3 rounded-full border-2 border-white border-t-transparent animate-spin" />
            )}
            {loading ? 'Đang lưu...' : 'Lưu'}
          </button>
        </div>
      </div>
    </div>
  )
}

// ─────────────────────────────────────────────────────────────
// PRODUCT CREATE/UPDATE MODAL
// ─────────────────────────────────────────────────────────────
/**
 * Modal tạo mới hoặc cập nhật sản phẩm.
 * - mode = 'create': gọi createProduct với ProductCreateRequest
 * - mode = 'update': gọi updateProduct(id) với ProductUpdateRequest
 */
function ProductFormModal({ mode, initial, categories, storeId, onClose, onSuccess }) {
  const isCreate = mode === 'create'

  const [form, setForm] = useState(
    isCreate ? EMPTY_CREATE_FORM : {
      productCode:  initial.productCode  ?? '',
      productName:  initial.productName  ?? '',
      description:  initial.description  ?? '',
      unit:         initial.unit         ?? '',
      images:       initial.images       ?? '',
      isActive:     initial.isActive     ?? true,
    }
  )
  const [errors, setErrors] = useState({})

  const mutation = useMutation({
    mutationFn: (data) =>
      isCreate
        ? createProduct({ ...data, storeId })        // POST /products
        : updateProduct(initial.productId, data),    // PUT /products/:id
    onSuccess: () => { onSuccess(); onClose() },
  })

  const validate = () => {
    const e = {}
    if (!form.productName?.trim()) e.productName = 'Vui lòng nhập tên sản phẩm'
    if (isCreate && !form.categoryId) e.categoryId = 'Vui lòng chọn danh mục'
    return e
  }

  const handleSubmit = () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }
    // Loại bỏ field rỗng để tránh BE nhận chuỗi rỗng không mong muốn
    const cleaned = Object.fromEntries(
      Object.entries(form).filter(([, v]) => v !== '')
    )
    mutation.mutate(cleaned)
  }

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }))

  return (
    <Modal
      title={isCreate ? '+ Thêm sản phẩm mới' : `Chỉnh sửa · ${initial?.productName}`}
      onClose={onClose} onSubmit={handleSubmit} loading={mutation.isPending} wide
    >
      {/* Tên + mã sản phẩm */}
      <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
        <Field label="Tên sản phẩm *" error={errors.productName}>
          <Inp placeholder="VD: Cà phê sữa đá" value={form.productName}
            onChange={e => set('productName', e.target.value)} error={errors.productName} />
        </Field>
        <Field label="Mã sản phẩm">
          <Inp placeholder="VD: CF001" value={form.productCode}
            onChange={e => set('productCode', e.target.value)} />
        </Field>
      </div>

      {/* Danh mục — chỉ hiện khi tạo mới */}
      {isCreate && (
        <Field label="Danh mục *" error={errors.categoryId}>
          <Sel value={form.categoryId} onChange={e => set('categoryId', Number(e.target.value))}>
            <option value="">-- Chọn danh mục --</option>
            {categories.map(c => (
              <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>
            ))}
          </Sel>
        </Field>
      )}

      {/* Đơn vị + giá (create) */}
      {isCreate ? (
        <div className="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <Field label="Đơn vị">
            <Inp placeholder="Ly, Hộp, Phần..." value={form.unit}
              onChange={e => set('unit', e.target.value)} />
          </Field>
          <Field label="Giá bán">
            <Inp type="number" placeholder="0" value={form.basePrice}
              onChange={e => set('basePrice', e.target.value)} />
          </Field>
          <Field label="Giá vốn">
            <Inp type="number" placeholder="0" value={form.costPrice}
              onChange={e => set('costPrice', e.target.value)} />
          </Field>
        </div>
      ) : (
        <Field label="Đơn vị">
          <Inp placeholder="Ly, Hộp, Phần..." value={form.unit}
            onChange={e => set('unit', e.target.value)} />
        </Field>
      )}

      {/* Mô tả */}
      <Field label="Mô tả">
        <Textarea placeholder="Mô tả sản phẩm..." value={form.description}
          onChange={e => set('description', e.target.value)} />
      </Field>

      {/* Images — hiện tại là text, sau đổi sang file upload */}
      <Field label="Ảnh (URL)">
        <Inp placeholder="https://..." value={form.images}
          onChange={e => set('images', e.target.value)} />
      </Field>

      {/* Trạng thái */}
      <label className="flex cursor-pointer items-center gap-2 text-xs font-medium text-[var(--text)]">
        <input type="checkbox" checked={form.isActive === true}
          onChange={e => set('isActive', Boolean(e.target.checked))}
          className="h-3.5 w-3.5 accent-emerald-500" />
        Sản phẩm đang kinh doanh
      </label>

      {mutation.isError && (
        <p className="rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] font-medium text-rose-500">
          ⚠ {mutation.error?.message ?? 'Có lỗi xảy ra'}
        </p>
      )}
    </Modal>
  )
}

// ─────────────────────────────────────────────────────────────
// VARIANT CREATE/UPDATE MODAL
// ─────────────────────────────────────────────────────────────
/**
 * Modal tạo mới hoặc cập nhật variant.
 * - mode = 'create': gọi createProductVariant với ProductVariantCreateRequest
 * - mode = 'update': gọi updateProductVariant(id) với ProductVariantUpdateRequest
 */
function VariantFormModal({ mode, initial, productId, onClose, onSuccess }) {
  const isCreate = mode === 'create'

  const [form, setForm] = useState(
    isCreate ? EMPTY_VARIANT_FORM : {
      variantName: initial.variantName  ?? '',
      variantType: initial.variantType  ?? 'SIZE',
      sku:         initial.sku          ?? '',
      barcode:     initial.barcode      ?? '',
      attributes:  initial.attributes   ?? '',
      price:       initial.price        ?? '',
      costPrice:   initial.costPrice    ?? '',
      weightGram:  initial.weightGram   ?? '',
      isActive:    initial.isActive     ?? true,
    }
  )
  const [errors, setErrors] = useState({})

  const mutation = useMutation({
    mutationFn: (data) =>
      isCreate
        ? createProductVariant({ ...data, productId })  // POST /product-variants
        : updateProductVariant(initial.variantId, data), // PUT /product-variants/:id
    onSuccess: () => { onSuccess(); onClose() },
  })

  const validate = () => {
    const e = {}
    if (!form.variantName?.trim()) e.variantName = 'Vui lòng nhập tên variant'
    return e
  }

  const handleSubmit = () => {
    const e = validate()
    if (Object.keys(e).length) { setErrors(e); return }
    const cleaned = Object.fromEntries(
      Object.entries(form).filter(([, v]) => v !== '')
    )
    mutation.mutate(cleaned)
  }

  const set = (k, v) => setForm(f => ({ ...f, [k]: v }))

  return (
    <Modal
      title={isCreate ? '+ Thêm biến thể' : `Chỉnh sửa · ${initial?.variantName}`}
      onClose={onClose} onSubmit={handleSubmit} loading={mutation.isPending}
    >
      <div className="grid grid-cols-2 gap-3">
        <Field label="Tên biến thể *" error={errors.variantName}>
          <Inp placeholder="VD: Size L, Không đường..." value={form.variantName}
            onChange={e => set('variantName', e.target.value)} error={errors.variantName} />
        </Field>
        <Field label="Loại">
          <Sel value={form.variantType} onChange={e => set('variantType', e.target.value)}>
            {VARIANT_TYPES.map(v => <option key={v.value} value={v.value}>{v.label}</option>)}
          </Sel>
        </Field>
      </div>
      <div className="grid grid-cols-2 gap-3">
        <Field label="Giá bán">
          <Inp type="number" placeholder="0" value={form.price}
            onChange={e => set('price', e.target.value)} />
        </Field>
        <Field label="Giá vốn">
          <Inp type="number" placeholder="0" value={form.costPrice}
            onChange={e => set('costPrice', e.target.value)} />
        </Field>
      </div>
      <div className="grid grid-cols-2 gap-3">
        <Field label="SKU">
          <Inp placeholder="CF-L-001" value={form.sku}
            onChange={e => set('sku', e.target.value)} />
        </Field>
        <Field label="Barcode">
          <Inp placeholder="8934573..." value={form.barcode}
            onChange={e => set('barcode', e.target.value)} />
        </Field>
      </div>
      <div className="grid grid-cols-2 gap-3">
        <Field label="Thuộc tính">
          <Inp placeholder="color=red, size=L" value={form.attributes}
            onChange={e => set('attributes', e.target.value)} />
        </Field>
        <Field label="Khối lượng (gram)">
          <Inp type="number" placeholder="250" value={form.weightGram}
            onChange={e => set('weightGram', e.target.value)} />
        </Field>
      </div>
      <label className="flex cursor-pointer items-center gap-2 text-xs font-medium text-[var(--text)]">
        <input type="checkbox" checked={form.isActive === true}
          onChange={e => set('isActive', Boolean(e.target.checked))}
          className="h-3.5 w-3.5 accent-emerald-500" />
        Biến thể đang kinh doanh
      </label>
      {mutation.isError && (
        <p className="rounded-lg bg-rose-500/10 px-3 py-2 text-[11px] font-medium text-rose-500">
          ⚠ {mutation.error?.message ?? 'Có lỗi xảy ra'}
        </p>
      )}
    </Modal>
  )
}

// ─────────────────────────────────────────────────────────────
// PRODUCT DETAIL PANEL
// ─────────────────────────────────────────────────────────────
/**
 * Panel bên phải — hiển thị thông tin chi tiết sản phẩm + danh sách variant.
 * Gọi GET /product-variants/all/:productId khi mở.
 */
function ProductDetailPanel({ product, onEdit, onClose, onVariantChange }) {
  const [variantModal, setVariantModal] = useState(null) // null | 'create' | variant obj

  // Fetch danh sách variant của sản phẩm
  const { data: res = [], isLoading: loadingVariants, refetch: refetchVariants } = useQuery({
    queryKey: ['variants', product.productId],
    queryFn: () => getProductVariantsByProductId(product.productId),
    enabled: !!product.productId,
  })

  const variants = res.data ?? []

  return (
    <div className="flex h-full flex-col">
      {/* Header */}
      <div className="flex items-center justify-between border-b border-[var(--border)] px-4 py-3 shrink-0">
        <p className="text-sm font-bold text-[var(--text)]">Chi tiết sản phẩm</p>
        <button type="button" onClick={onClose}
          className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
            text-[var(--muted)] hover:border-[var(--text)] transition-all">
          ✕
        </button>
      </div>

      <div className="flex-1 overflow-auto">
        {/* Avatar + tên */}
        <div className="flex items-center gap-3 border-b border-[var(--border)] px-4 py-4">
          {product.images ? (
            <img src={product.images} alt={product.productName}
              className="h-14 w-14 rounded-xl object-cover border border-[var(--border)]" />
          ) : (
            <div className="flex h-14 w-14 items-center justify-center rounded-xl
              bg-emerald-500/10 text-xl font-black text-emerald-500 border border-emerald-500/20">
              {getInitials(product.productName)}
            </div>
          )}
          <div>
            <p className="text-sm font-bold text-[var(--text)]">{product.productName}</p>
            <p className="text-[10px] text-[var(--muted)]">{product.productCode || '—'}</p>
            <span className={`mt-1 inline-block rounded-full border px-2 py-0.5 text-[9px] font-bold
              ${product.isActive
                ? 'bg-emerald-500/10 text-emerald-500 border-emerald-500/30'
                : 'bg-rose-500/10 text-rose-400 border-rose-500/30'}`}>
              {product.isActive ? 'Đang bán' : 'Ngừng bán'}
            </span>
          </div>
        </div>

        {/* Thông tin sản phẩm */}
        <div className="border-b border-[var(--border)] px-4 py-3 space-y-2.5">
          {[
            { label: 'Giá bán',  value: fmtMoney(product.basePrice) },
            { label: 'Giá vốn',  value: fmtMoney(product.costPrice) },
            { label: 'Đơn vị',   value: product.unit || '—' },
            { label: 'Mô tả',    value: product.description || '—' },
            { label: 'Ngày tạo', value: fmtDate(product.createdAt) },
            { label: 'Cập nhật', value: fmtDate(product.updatedAt) },
          ].map(row => (
            <div key={row.label} className="flex items-start justify-between gap-2">
              <span className="shrink-0 text-[11px] font-medium text-[var(--muted)]">{row.label}</span>
              <span className="text-right text-[11px] font-semibold text-[var(--text)]">{row.value}</span>
            </div>
          ))}
        </div>

        {/* Danh sách variant */}
        <div className="px-4 py-3">
          <div className="mb-2 flex items-center justify-between">
            <p className="text-[11px] font-bold text-[var(--muted)] uppercase tracking-wide">
              Biến thể ({Array.isArray(variants) ? variants.length : 0})
            </p>
            <button type="button" onClick={() => setVariantModal('create')}
              className="rounded-lg border border-emerald-500/40 bg-emerald-500/10 px-2 py-1
                text-[10px] font-semibold text-emerald-500 hover:bg-emerald-500/20 transition-colors">
              + Thêm
            </button>
          </div>

          {loadingVariants ? (
            <div className="flex items-center justify-center gap-2 py-4 text-xs text-[var(--muted)]">
              <span className="h-3 w-3 animate-spin rounded-full border-2 border-[var(--border)] border-t-emerald-500" />
              Đang tải...
            </div>
          ) : !Array.isArray(variants) || variants.length === 0 ? (
            <p className="py-4 text-center text-[11px] text-[var(--muted)]/50">
              Chưa có biến thể nào
            </p>
          ) : (
            <div className="space-y-1.5">
              {variants.map(v => (
                <div key={v.variantId}
                  className="flex items-center justify-between rounded-xl border border-[var(--border)]
                    bg-[var(--surface-2)] px-3 py-2 text-xs">
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-1.5 flex-wrap">
                      <p className="font-semibold text-[var(--text)] truncate">{v.variantName}</p>
                      <span className="rounded-full bg-[var(--surface)] border border-[var(--border)]
                        px-1.5 py-0.5 text-[9px] font-medium text-[var(--muted)]">
                        {VARIANT_TYPE_LABEL[v.variantType] ?? v.variantType}
                      </span>
                      {!v.isActive && (
                        <span className="rounded-full bg-rose-500/10 px-1.5 py-0.5 text-[9px] font-bold text-rose-400">
                          Ngừng
                        </span>
                      )}
                    </div>
                    <p className="mt-0.5 text-[10px] text-[var(--muted)]">
                      {v.sku && `SKU: ${v.sku}`}
                      {v.sku && v.price ? ' · ' : ''}
                      {v.price ? fmtMoney(v.price) : ''}
                    </p>
                  </div>
                  <button type="button" onClick={() => setVariantModal(v)}
                    className="ml-2 shrink-0 rounded-lg border border-[var(--border)] px-2 py-1
                      text-[10px] text-[var(--muted)] hover:border-emerald-500 hover:text-emerald-500
                      transition-all">
                    Sửa
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Footer action */}
      <div className="flex gap-2 border-t border-[var(--border)] px-4 py-3 shrink-0">
        <button type="button" onClick={onEdit}
          className="flex-1 rounded-lg bg-emerald-500 py-2 text-xs font-bold
            text-white hover:bg-emerald-600 transition-colors">
          Chỉnh sửa sản phẩm
        </button>
      </div>

      {/* Variant modal */}
      {variantModal !== null && (
        <VariantFormModal
          mode={variantModal === 'create' ? 'create' : 'update'}
          initial={variantModal === 'create' ? null : variantModal}
          productId={product.productId}
          onClose={() => setVariantModal(null)}
          onSuccess={() => {
            refetchVariants()          // Refresh variant list
            onVariantChange?.()        // Notify parent nếu cần
          }}
        />
      )}
    </div>
  )
}

// ─────────────────────────────────────────────────────────────
// MAIN PAGE
// ─────────────────────────────────────────────────────────────
export function ProductsPage() {
  const { currentStoreId } = useStore()
  const queryClient = useQueryClient()

  // ── Filter state ──────────────────────────────────────────
  const [keyword, setKeyword]         = useState('')
  const [categoryId, setCategoryId]   = useState('')
  const [isActiveFilter, setIsActive] = useState('')  // '' | 'true' | 'false'
  const [page, setPage]               = useState(1)
  const PAGE_SIZE = 20

  /**
   * Build filter params cho GET /products/filter
   * Lưu ý: BE dùng @ModelAttribute + @Getter → Spring binding không đọc được null.
   * Chỉ append field khi có giá trị thực, không gửi chuỗi rỗng hay null.
   */
  const filterParams = useMemo(() => {
    const f = { storeId: currentStoreId }
    if (keyword.trim())     f.keyword    = keyword.trim()
    if (categoryId)         f.categoryId = Number(categoryId)
    if (isActiveFilter !== '') f.isActive = isActiveFilter === 'true'
    return f
  }, [keyword, categoryId, isActiveFilter, currentStoreId])

  // ── Fetch products ────────────────────────────────────────
  const { data: productPage, isLoading } = useQuery({
    queryKey: ['products', page, filterParams],
    queryFn: () => getProducts(page, PAGE_SIZE, filterParams),
    enabled: !!currentStoreId,
    keepPreviousData: true,
  })

  const products     = productPage?.data.content ?? []
  const totalPages   = productPage?.data.totalPages ?? 1
  const totalElements = productPage?.data.totalElements ?? 0

  // ── Fetch categories cho filter + form ────────────────────
  const { data: catData } = useQuery({
    queryKey: ['categories', currentStoreId],
    queryFn: () => getCategories(currentStoreId),
    enabled: !!currentStoreId,
  })
  const categories = catData?.content ?? []

  const invalidateProducts = () =>
    queryClient.invalidateQueries({ queryKey: ['products'] })

  // ── Panel / modal state ───────────────────────────────────
  const [selected, setSelected]     = useState(null)   // product | null
  const [productModal, setModal]    = useState(null)    // null | 'create' | product obj

  return (
    <div className="flex h-[calc(100vh-3.5rem)] overflow-hidden text-[var(--text)]">

      {/* ── LEFT: danh sách sản phẩm ── */}
      <div className={`flex flex-col transition-all duration-300
        ${selected ? 'w-full md:w-[60%] lg:w-[65%]' : 'w-full'}`}>

        {/* Header */}
        <div className="flex flex-wrap items-center justify-between gap-3
          border-b border-[var(--border)] px-4 py-3 shrink-0">
          <div>
            <h1 className="text-base font-bold sm:text-lg text-[var(--text)]">Sản phẩm</h1>
            <p className="text-xs text-[var(--muted)]">
              {totalElements.toLocaleString('vi-VN')} sản phẩm
            </p>
          </div>
          <button type="button" onClick={() => setModal('create')}
            className="rounded-xl bg-emerald-500 px-4 py-2 text-xs font-bold text-white
              hover:bg-emerald-600 active:scale-95 transition-all shadow-sm shadow-emerald-500/30">
            + Thêm sản phẩm
          </button>
        </div>

        {/* Filter bar */}
        <div className="flex flex-wrap gap-2 border-b border-[var(--border)] px-4 py-2.5 shrink-0">
          {/* Tìm kiếm theo tên / mã */}
          <input
            className="min-w-[180px] flex-1 rounded-lg border border-[var(--border)] bg-[var(--input)]
              px-3 py-1.5 text-xs text-[var(--text)] placeholder:text-[var(--muted)]
              focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all"
            placeholder="Tìm theo tên, mã sản phẩm..."
            value={keyword}
            onChange={e => { setKeyword(e.target.value); setPage(1) }}
          />
          {/* Lọc theo danh mục */}
          <select
            className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2.5 py-1.5
              text-xs text-[var(--text)] focus:outline-none focus:ring-2 focus:ring-emerald-500/30
              focus:border-emerald-500 transition-all"
            value={categoryId}
            onChange={e => { setCategoryId(e.target.value); setPage(1) }}
          >
            <option value="">Tất cả danh mục</option>
            {categories.map(c => (
              <option key={c.categoryId} value={c.categoryId}>{c.categoryName}</option>
            ))}
          </select>
          {/* Lọc theo trạng thái */}
          <select
            className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2.5 py-1.5
              text-xs text-[var(--text)] focus:outline-none focus:ring-2 focus:ring-emerald-500/30
              focus:border-emerald-500 transition-all"
            value={isActiveFilter}
            onChange={e => { setIsActive(e.target.value); setPage(1) }}
          >
            <option value="">Tất cả trạng thái</option>
            <option value="true">Đang bán</option>
            <option value="false">Ngừng bán</option>
          </select>
        </div>

        {/* Bảng sản phẩm */}
        <div className="flex-1 overflow-auto">
          {isLoading ? (
            <div className="flex items-center justify-center gap-2 py-20 text-sm text-[var(--muted)]">
              <span className="h-4 w-4 animate-spin rounded-full border-2 border-[var(--border)] border-t-emerald-500" />
              Đang tải...
            </div>
          ) : products.length === 0 ? (
            <div className="flex flex-col items-center justify-center gap-3 py-20">
              <span className="text-4xl opacity-30">📦</span>
              <p className="text-sm text-[var(--muted)]">Không tìm thấy sản phẩm nào</p>
            </div>
          ) : (
            <table className="min-w-full text-left text-xs text-[var(--text)]">
              <thead className="sticky top-0 z-10 bg-[var(--surface-2)] text-[var(--muted)]">
                <tr>
                  <th className="px-4 py-2.5 font-semibold">Sản phẩm</th>
                  <th className="hidden px-3 py-2.5 font-semibold sm:table-cell">Danh mục</th>
                  <th className="px-3 py-2.5 text-right font-semibold">Giá bán</th>
                  <th className="hidden px-3 py-2.5 text-right font-semibold md:table-cell">Giá vốn</th>
                  <th className="px-3 py-2.5 font-semibold">Trạng thái</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-[var(--border)]">
                {products.map(p => (
                  <tr
                    key={p.productId}
                    onClick={() => setSelected(prev => prev?.productId === p.productId ? null : p)}
                    className={`cursor-pointer transition-colors hover:bg-[var(--surface-2)]
                      ${selected?.productId === p.productId
                        ? 'bg-emerald-500/5 border-l-2 border-l-emerald-500'
                        : ''}`}
                  >
                    {/* Tên + mã */}
                    <td className="px-4 py-2.5">
                      <div className="flex items-center gap-2.5">
                        {/* Avatar: ảnh hoặc initials */}
                        {p.images ? (
                          <img src={p.images} alt={p.productName}
                            className="h-8 w-8 shrink-0 rounded-lg object-cover border border-[var(--border)]" />
                        ) : (
                          <div className="flex h-8 w-8 shrink-0 items-center justify-center
                            rounded-lg bg-emerald-500/10 text-[11px] font-black text-emerald-500">
                            {getInitials(p.productName)}
                          </div>
                        )}
                        <div>
                          <p className="font-semibold text-[var(--text)]">{p.productName}</p>
                          <p className="text-[10px] text-[var(--muted)]">{p.productCode || '—'}</p>
                        </div>
                      </div>
                    </td>
                    {/* Danh mục — ẩn mobile */}
                    <td className="hidden px-3 py-2.5 text-[var(--muted)] sm:table-cell">
                      {p.categoryName ?? '—'}
                    </td>
                    {/* Giá bán */}
                    <td className="px-3 py-2.5 text-right font-semibold text-emerald-500">
                      {fmtMoney(p.basePrice)}
                    </td>
                    {/* Giá vốn — ẩn mobile */}
                    <td className="hidden px-3 py-2.5 text-right text-[var(--muted)] md:table-cell">
                      {fmtMoney(p.costPrice)}
                    </td>
                    {/* Trạng thái */}
                    <td className="px-3 py-2.5">
                      <span className={`rounded-full border px-1.5 py-0.5 text-[9px] font-bold
                        ${p.isActive
                          ? 'bg-emerald-500/10 text-emerald-500 border-emerald-500/30'
                          : 'bg-rose-500/10 text-rose-400 border-rose-500/30'}`}>
                        {p.isActive ? 'Đang bán' : 'Ngừng bán'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>

        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex items-center justify-between border-t border-[var(--border)]
            px-4 py-2.5 text-xs text-[var(--muted)] shrink-0">
            <span>Trang {page} / {totalPages}</span>
            <div className="flex gap-1">
              <button type="button" disabled={page <= 1} onClick={() => setPage(p => p - 1)}
                className="rounded-lg border border-[var(--border)] px-2.5 py-1 disabled:opacity-40
                  hover:border-emerald-500 hover:text-emerald-500 transition-all">
                ‹ Trước
              </button>
              <button type="button" disabled={page >= totalPages} onClick={() => setPage(p => p + 1)}
                className="rounded-lg border border-[var(--border)] px-2.5 py-1 disabled:opacity-40
                  hover:border-emerald-500 hover:text-emerald-500 transition-all">
                Tiếp ›
              </button>
            </div>
          </div>
        )}
      </div>

      {/* ── RIGHT: detail panel — ẩn mobile, hiện từ md ── */}
      {selected && (
        <div className="hidden border-l border-[var(--border)] bg-[var(--surface-solid)]
          md:flex md:w-[40%] lg:w-[35%] flex-col">
          <ProductDetailPanel
            product={selected}
            onEdit={() => setModal(selected)}
            onClose={() => setSelected(null)}
            onVariantChange={invalidateProducts}
          />
        </div>
      )}

      {/* ── PRODUCT FORM MODAL ── */}
      {productModal !== null && (
        <ProductFormModal
          mode={productModal === 'create' ? 'create' : 'update'}
          initial={productModal === 'create' ? null : productModal}
          categories={categories}
          storeId={currentStoreId}
          onClose={() => setModal(null)}
          onSuccess={() => {
            invalidateProducts()
            if (productModal !== 'create') setSelected(null)
          }}
        />
      )}
    </div>
  )
}