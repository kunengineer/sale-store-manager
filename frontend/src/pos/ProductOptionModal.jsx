import { useState, useMemo, useEffect } from "react"

function ProductOptionModal({ product, onAdd, onClose }) {
  if (!product) return null

  const baseVariants = useMemo(() => {
    return product?.variants?.filter(v => v.variantType === "BASE") || []
  }, [product])

  console.log('baseVariants', baseVariants)
  console.log('product', product)

  const toppingVariants = useMemo(() => {
    return product?.variants?.filter(v => v.variantType === "TOPPING") || []
  }, [product])

  const [selectedBase, setSelectedBase] = useState(null)
  const [selectedToppings, setSelectedToppings] = useState([])

  // set mặc định size đầu tiên
  useEffect(() => {
    if (baseVariants.length > 0) {
      setSelectedBase(baseVariants[0])
    }
  }, [baseVariants])

  const toggleTopping = (topping) => {
    setSelectedToppings(prev =>
      prev.some(t => t.variantId === topping.variantId)
        ? prev.filter(t => t.variantId !== topping.variantId)
        : [...prev, topping]
    )
  }

  const totalPrice = useMemo(() => {
    const basePrice = selectedBase?.price || 0
    const toppingPrice = selectedToppings.reduce(
      (sum, t) => sum + t.price,
      0
    )
    return basePrice + toppingPrice
  }, [selectedBase, selectedToppings])

  const handleAdd = () => {
    if (!selectedBase) return

    onAdd({
      productId: product.productId,
      productName: product.productName,
      base: selectedBase,
      toppings: selectedToppings,
      price: totalPrice
    })

    onClose()
  }

  return (
    <div className="fixed inset-0 z-50 flex items-end md:items-center justify-center bg-black/50">
      
      {/* Modal Box */}
      <div className="w-full md:w-[480px] max-h-[90vh] bg-[var(--surface-solid)] rounded-t-2xl md:rounded-2xl shadow-xl flex flex-col">

        {/* Header */}
        <div className="flex items-center justify-between p-4 border-b border-[var(--border)]">
          <h2 className="text-lg font-semibold text-[var(--text)]">
            {product.productName}
          </h2>
          <button
            onClick={onClose}
            className="text-sm text-[var(--muted)] hover:text-emerald-500"
          >
            Đóng
          </button>
        </div>

        {/* Body */}
        <div className="flex-1 overflow-y-auto p-4 space-y-5">

          {/* SIZE */}
          <div>
            <p className="mb-2 text-sm font-medium text-emerald-500">
              Chọn size (bắt buộc)
            </p>

            <div className="grid grid-cols-2 gap-2">
              {baseVariants.map(b => (
                <button
                  key={b.variantId}
                  onClick={() => setSelectedBase(b)}
                  className={`rounded-xl border px-3 py-2 text-sm transition
                    ${
                      selectedBase?.variantId === b.variantId
                        ? "border-emerald-500 bg-emerald-500/10 text-emerald-500"
                        : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)]"
                    }`}
                >
                  <div className="flex justify-between">
                    <span>{b.variantName}</span>
                    <span>{b.price.toLocaleString("vi-VN")}đ</span>
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* TOPPING */}
          {toppingVariants.length > 0 && (
            <div>
              <p className="mb-2 text-sm font-medium text-yellow-500">
                Chọn topping (tuỳ chọn)
              </p>

              <div className="space-y-2">
                {toppingVariants.map(t => {
                  const checked = selectedToppings.some(
                    s => s.variantId === t.variantId
                  )

                  return (
                    <label
                      key={t.variantId}
                      className={`flex items-center justify-between rounded-lg border px-3 py-2 text-sm cursor-pointer transition
                        ${
                          checked
                            ? "border-emerald-500 bg-emerald-500/10"
                            : "border-[var(--border)] bg-[var(--surface-2)]"
                        }`}
                    >
                      <div className="flex items-center gap-2">
                        <input
                          type="checkbox"
                          checked={checked}
                          onChange={() => toggleTopping(t)}
                          className="accent-emerald-500"
                        />
                        <span>{t.variantName}</span>
                      </div>
                      <span className="text-[var(--muted)]">
                        +{t.price.toLocaleString("vi-VN")}đ
                      </span>
                    </label>
                  )
                })}
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="border-t border-[var(--border)] p-4 space-y-2">
          <div className="flex justify-between text-sm">
            <span className="text-[var(--muted)]">Tổng tiền</span>
            <span className="font-semibold text-emerald-500">
              {totalPrice.toLocaleString("vi-VN")}đ
            </span>
          </div>

          <button
            onClick={handleAdd}
            disabled={!selectedBase}
            className="w-full rounded-xl bg-emerald-500 py-3 text-sm font-semibold text-slate-950 hover:opacity-90 disabled:opacity-50"
          >
            Thêm vào đơn
          </button>
        </div>
      </div>
    </div>
  )
}

export default ProductOptionModal