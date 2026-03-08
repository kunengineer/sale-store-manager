import { useMemo, useState, useRef, useEffect } from "react";
import { usePos } from "./PosContext";
import { useStore } from "../store/StoreContext";
import { createOrder } from "../data/services/orderService";
import CustomerSearchBox from "./CustomerSearchBox";

function PaymentModal({
  open,
  onClose,
  total,
  customerPaid,
  onChangeCustomerPaid,
  discount,
  onChangeDiscount,
  surcharge,
  onChangeSurcharge,
  change,
}) {
  if (!open) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60">
      <div className="w-full max-w-lg rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-4 text-[var(--text)] shadow-xl">
        <div className="mb-3 flex items-center justify-between">
          <div>
            <h2 className="text-sm font-semibold text-slate-50">
              Thanh toán hóa đơn
            </h2>
            <p className="text-xs text-slate-400">
              Tổng {total.toLocaleString("vi-VN")}đ
            </p>
          </div>
          <button
            type="button"
            onClick={onClose}
            className="rounded-full border border-slate-700 px-2 py-1 text-xs text-slate-300 hover:border-emerald-500"
          >
            Đóng
          </button>
        </div>
        <div className="grid gap-4 md:grid-cols-[1.2fr,1fr]">
          <div className="space-y-3">
            <label className="block text-xs text-slate-300">
              Số tiền khách đưa
              <input
                autoFocus
                type="number"
                value={customerPaid}
                onChange={(e) =>
                  onChangeCustomerPaid(Number(e.target.value) || 0)
                }
                className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-sm text-[var(--text)] focus:outline-none focus:ring-2 focus:ring-emerald-500"
                placeholder="Nhập số tiền..."
              />
            </label>
            <div className="flex gap-2 text-xs">
              <label className="flex-1">
                <span className="mb-1 block text-[11px] text-slate-300">
                  Giảm giá
                </span>
                <input
                  type="number"
                  value={discount}
                  onChange={(e) =>
                    onChangeDiscount(Number(e.target.value) || 0)
                  }
                  className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                  placeholder="0đ"
                />
              </label>
              <label className="flex-1">
                <span className="mb-1 block text-[11px] text-slate-300">
                  Phụ thu
                </span>
                <input
                  type="number"
                  value={surcharge}
                  onChange={(e) =>
                    onChangeSurcharge(Number(e.target.value) || 0)
                  }
                  className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                  placeholder="0đ"
                />
              </label>
            </div>
            <div className="flex flex-wrap gap-2">
              {["50.000", "100.000", "200.000", "500.000"].map((val) => (
                <button
                  key={val}
                  type="button"
                  className="rounded-full border border-slate-700 bg-slate-900 px-3 py-1 text-xs text-slate-200 hover:border-emerald-500"
                >
                  {val}đ
                </button>
              ))}
            </div>
          </div>
          <div className="space-y-2 rounded-lg border border-[var(--border)] bg-[var(--surface-2)] p-3 text-xs text-[var(--muted)]">
            <div className="flex justify-between">
              <span>Tổng thanh toán</span>
              <span className="font-semibold text-emerald-500">
                {total.toLocaleString("vi-VN")}đ
              </span>
            </div>
            <div className="flex justify-between">
              <span>Tiền khách đưa</span>
              <span>{customerPaid.toLocaleString("vi-VN")}đ</span>
            </div>
            <div className="flex justify-between">
              <span>Giảm giá</span>
              <span>{discount.toLocaleString("vi-VN")}đ</span>
            </div>
            <div className="flex justify-between">
              <span>Phụ thu</span>
              <span>{surcharge.toLocaleString("vi-VN")}đ</span>
            </div>
            <div className="flex justify-between text-emerald-400">
              <span>Tiền thừa</span>
              <span className="font-semibold">
                {change.toLocaleString("vi-VN")}đ
              </span>
            </div>
            <div className="mt-3">
              <span className="mb-1 block text-xs text-slate-400">
                Phương thức thanh toán
              </span>
              <div className="flex flex-wrap gap-2">
                {["Tiền mặt", "Thẻ", "Chuyển khoản", "Ví điện tử"].map((m) => (
                  <button
                    key={m}
                    type="button"
                    className="rounded-full border border-emerald-500/60 bg-emerald-500/10 px-3 py-1 text-[11px] text-emerald-200"
                  >
                    {m}
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
        <div className="mt-4 flex items-center justify-between">
          <label className="flex items-center gap-2 text-xs text-slate-300">
            <input
              type="checkbox"
              className="h-3 w-3 rounded border-slate-600"
            />
            In hóa đơn sau khi thanh toán
          </label>
          <div className="flex gap-2">
            <button
              type="button"
              className="rounded-lg border border-slate-700 px-3 py-1.5 text-xs text-slate-200"
            >
              Thanh toán
            </button>
            <button
              type="button"
              className="rounded-lg bg-emerald-500 px-3 py-1.5 text-xs font-semibold text-slate-950"
            >
              Thanh toán & In
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

function ChangeTableModal({ open, onClose }) {
  const { tables, selectedTable, selectTable } = usePos();
  if (!open) return null;
  const handleChange = (id) => {
    selectTable(id);
    onClose();
  };
  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/60">
      <div className="w-full max-w-sm rounded-xl border border-slate-800 bg-slate-900 p-3 text-xs text-slate-100">
        <div className="mb-2 flex items-center justify-between">
          <p className="text-sm font-semibold text-slate-50">Đổi bàn</p>
          <button
            type="button"
            onClick={onClose}
            className="rounded-full border border-slate-700 px-2 py-0.5 text-[11px] text-slate-300"
          >
            Đóng
          </button>
        </div>
        <p className="mb-2 text-[11px] text-slate-400">
          Chọn bàn muốn chuyển hoá đơn hiện tại sang.
        </p>
        <div className="grid grid-cols-3 gap-2">
          {tables.map((t) => (
            <button
              key={t.id}
              type="button"
              onClick={() => handleChange(t.id)}
              className={`rounded-lg border px-2 py-1.5 text-left text-[11px] ${
                selectedTable?.id === t.id
                  ? "border-emerald-500 bg-emerald-500/10"
                  : "border-slate-700 bg-slate-900"
              }`}
            >
              <span className="block text-xs font-semibold text-slate-50">
                Bàn {t.name}
              </span>
              <span className="block text-[10px] text-slate-400">
                {t.status === "using"
                  ? `Đang dùng · ${t.guests ?? 0} khách`
                  : t.status === "reserved"
                    ? "Đã đặt chỗ"
                    : "Trống"}
              </span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

// ============================================================
// POS ORDER COLUMN
// ============================================================
export function PosOrderColumn() {
  const [openPayment, setOpenPayment] = useState(false);
  const [openChangeTable, setOpenChangeTable] = useState(false);

  // Dữ liệu từ PosContext
  // selectedTable  : bàn đang được chọn ở cột trái
  // orderItems     : danh sách món [{id, productName, base, toppings, note, qty, price}]
  // changeItemQty  : (itemId, delta) => void — tăng/giảm số lượng món
  // subtotal       : tổng tiền chưa áp giảm giá/phụ thu (tính sẵn trong context)
  const {
    selectedTable,
    orderItems,
    changeItemQty,
    subtotal,
    currentOrderId,
    setCurrentOrderId,
    updateItemNote,
  } = usePos();
  const { theme } = useStore();

  const [discount, setDiscount] = useState(0);
  const [surcharge, setSurcharge] = useState(0);
  const [customerPaid, setCustomerPaid] = useState(0);

  // ── DRAFT / UNSAVED STATE ─────────────────────────────────
  // hasUnsaved : true khi orderItems thay đổi so với lần lưu tạm gần nhất
  //              → highlight nút Lưu tạm + khoá Thanh toán / Gộp bàn / Chuyển bàn
  // draftLoading: đang gọi API lưu tạm
  const [hasUnsaved, setHasUnsaved] = useState(false);
  const [draftLoading, setDraftLoading] = useState(false);

  // Lưu snapshot JSON của lần lưu tạm gần nhất dùng useRef
  // (không dùng useState vì thay đổi ref không cần trigger re-render)
  const lastSavedSnapshot = useRef(JSON.stringify(orderItems));

  const [customer, setCustomer] = useState(null);

  // So sánh orderItems hiện tại với snapshot — bật/tắt cờ unsaved
  useEffect(() => {
    const current = JSON.stringify(orderItems);
    setHasUnsaved(current !== lastSavedSnapshot.current);
  }, [orderItems]);

  // ── TÍNH TIỀN ─────────────────────────────────────────────
  const total = useMemo(
    () => Math.max(0, subtotal - discount + surcharge),
    [subtotal, discount, surcharge],
  );
  const change = useMemo(
    () => Math.max(0, customerPaid - total),
    [customerPaid, total],
  );

  // ── THỜI GIAN BÀN ─────────────────────────────────────────
  const tableTimeText = useMemo(() => {
    if (!selectedTable) return null;
    const src =
      selectedTable.status === "using"
        ? selectedTable.openedAt
        : selectedTable.status === "reserved"
          ? selectedTable.reservedAt
          : null;
    if (!src) return null;
    const d = new Date(src);
    return `${d.toLocaleDateString("vi-VN")} · ${d.toLocaleTimeString("vi-VN", { hour: "2-digit", minute: "2-digit" })}`;
  }, [selectedTable]);

  // ── LƯU TẠM ──────────────────────────────────────────────
  // Thêm note state vào component
  const [orderNote, setOrderNote] = useState("");

  const handleDraftOrder = async () => {
    if (!hasUnsaved || draftLoading) return;
    setDraftLoading(true);
    if (!selectedTable) {
      window.alert("Vui lòng chọn bàn trước khi lưu tạm.");
      setDraftLoading(false);
      return;
    }
    try {
      const newItems = orderItems
        .filter((item) => !item.orderItemId) // chỉ item chưa có trên BE
        .map((item) => ({
          productVariantId: item.base?.variantId,
          quantity: item.qty,
          discountPct: 0,
          note: item.note ?? "",
        }));

      if (currentOrderId) {
        // Order đã tồn tại → chỉ gửi items mới, tránh duplicate
        if (newItems.length > 0) {
          // TODO: const res = await addItemsToOrder(currentOrderId, newItems)
        }
      } else {
        // Chưa có order → tạo mới, BE tự resolve storeId & employeeId từ JWT
        const payload = {
          tableId: selectedTable.id,
          customerId: customer?.customerId,
          note: orderNote,
          subtotal: subtotal,
          items: orderItems.map((item) => ({
            productVariantId: item.base?.variantId,
            quantity: item.qty,
            discountPct: 0,
            note: item.note ?? "",
          })),
        };
        console.log("Payload tạo order:", payload);
        const res = await createOrder(payload);
        console.log("Res tạo order:", res);
        // setCurrentOrderId(res.orderId)
      }

      lastSavedSnapshot.current = JSON.stringify(orderItems);
      setHasUnsaved(false);
    } catch (err) {
      console.error("Lưu tạm thất bại:", err);
      // TODO: toast.error('Lưu tạm thất bại', err.message)
    } finally {
      setDraftLoading(false);
    }
  };

  // Biến tổng hợp — true = khoá Thanh toán, Gộp bàn, Chuyển bàn
  const isLocked = hasUnsaved;

  return (
    <>
      <section
        className={`flex w-[28%] min-w-[260px] max-w-[360px] flex-col rounded-xl border ${
          theme === "dark"
            ? "border-[var(--border)] bg-[var(--surface)]"
            : "border-[var(--border)] bg-[var(--surface-solid)]"
        }`}
      >
        {/* Header: tên bàn + nút đổi bàn */}
        <div className="flex items-start justify-between border-b border-[var(--border)] px-3 py-2">
          <div className="flex flex-col">
            <div className="flex items-center gap-2">
              <p className="text-sm font-semibold text-[var(--text)]">
                {selectedTable ? `Bàn ${selectedTable.name}` : "Chưa chọn bàn"}
              </p>
              {tableTimeText && (
                <span className="inline-block rounded-full bg-emerald-500/10 px-2 py-0.5 text-[10px] text-emerald-400">
                  {selectedTable.status === "using" ? "Mở lúc" : "Đặt lúc"}{" "}
                  {tableTimeText}
                </span>
              )}
            </div>
            <p className="text-xs text-emerald-600">
              {selectedTable
                ? selectedTable.status === "using"
                  ? `Đang phục vụ · ${selectedTable.guests ?? 0} khách`
                  : selectedTable.status === "reserved"
                    ? "Đã đặt chỗ"
                    : "Trống"
                : "Chọn bàn để tạo hóa đơn"}
            </p>
          </div>
          {/* Đổi bàn — khoá khi unsaved */}
          <button
            type="button"
            disabled={isLocked}
            title={isLocked ? "Lưu tạm đơn trước khi đổi bàn" : ""}
            onClick={() => setOpenChangeTable(true)}
            className={`rounded-lg border px-3 py-1.5 text-[11px] transition-all ${
              isLocked
                ? "cursor-not-allowed border-slate-800 bg-[var(--surface-2)] text-slate-600"
                : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)] hover:border-emerald-500"
            }`}
          >
            Đổi bàn
          </button>
        </div>

        {customer ? (
          <div className="mx-3 mt-2 flex items-center justify-between rounded-lg border bg-emerald-50 px-3 py-2 text-xs">
            <div>
              <div className="font-medium">{customer.fullName}</div>
              <div className="text-gray-500">{customer.phone}</div>
            </div>

            <button onClick={() => setCustomer(null)} className="text-red-500">
              ✕
            </button>
          </div>
        ) : (
          <CustomerSearchBox onSelect={setCustomer} />
        )}

        {/* Danh sách món */}
        <div className="flex-1 divide-y divide-[var(--border)] overflow-auto">
          {orderItems.map((item) => (
            <div
              key={item.id}
              className="flex items-start justify-between px-3 py-2 text-xs"
            >
              <div className="flex-1">
                <p className="text-sm font-medium">
                  {item.productName} - {item.base?.variantName}
                </p>
                {item.toppings?.map((t) => (
                  <p
                    key={t.variantId}
                    className="ml-2 text-[11px] text-[var(--muted)]"
                  >
                    + {t.variantName}
                  </p>
                ))}
                <input
                  type="text"
                  value={item.note ?? ""}
                  onChange={(e) => updateItemNote(item.id, e.target.value)}
                  placeholder="Ghi chú món... (ít đá, không đường...)"
                  className="mt-1 w-full rounded border border-[var(--border)] bg-transparent px-2 py-0.5 text-[10px] text-[var(--muted)] placeholder:text-[var(--muted)]/50 focus:outline-none focus:border-emerald-500/50 transition-colors"
                />
              </div>
              <div className="flex flex-col items-end gap-1">
                <p className="text-sm font-semibold text-[var(--text)]">
                  {(item.price * item.qty).toLocaleString("vi-VN")}đ
                </p>
                <div className="inline-flex items-center gap-1 rounded-full border border-[var(--border)] bg-[var(--surface-2)] px-1.5 py-0.5 text-[10px] text-[var(--text)]">
                  <button
                    type="button"
                    onClick={() => changeItemQty(item.id, -1)}
                  >
                    -
                  </button>
                  <span>{item.qty}</span>
                  <button
                    type="button"
                    onClick={() => changeItemQty(item.id, 1)}
                  >
                    +
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Footer */}
        <div className="space-y-2 border-t border-[var(--border)] p-3">
          {/* Nút phụ */}
          <div className="mb-2 flex flex-wrap gap-1 text-[10px]">
            {/* Gộp bàn — khoá khi unsaved */}
            <button
              type="button"
              disabled={isLocked}
              title={isLocked ? "Lưu tạm đơn trước khi gộp bàn" : ""}
              onClick={() =>
                window.alert("Gộp bàn sẽ được xử lý sau khi nối API.")
              }
              className={`flex-1 rounded-full border px-2 py-1 transition-all ${
                isLocked
                  ? "cursor-not-allowed border-slate-800 bg-[var(--surface-2)] text-slate-600"
                  : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)] hover:border-amber-500"
              }`}
            >
              Gộp bàn
            </button>

            {/* Chuyển bàn — khoá khi unsaved */}
            <button
              type="button"
              disabled={isLocked}
              title={isLocked ? "Lưu tạm đơn trước khi chuyển bàn" : ""}
              onClick={() => setOpenChangeTable(true)}
              className={`flex-1 rounded-full border px-2 py-1 transition-all ${
                isLocked
                  ? "cursor-not-allowed border-slate-800 bg-[var(--surface-2)] text-slate-600"
                  : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)] hover:border-amber-500"
              }`}
            >
              Chuyển bàn
            </button>

            {/* Huỷ đơn — không khoá */}
            <button
              type="button"
              className="flex-1 rounded-full border border-[var(--border)] bg-[var(--surface-2)] px-2 py-1 text-[var(--text)] hover:border-rose-500 transition-all"
              onClick={() =>
                window.alert("Hủy đơn sẽ được xử lý sau khi nối API.")
              }
            >
              Hủy đơn
            </button>
          </div>

          <label className="block text-[11px] text-[var(--muted)]">
            Ghi chú đơn hàng
            <textarea
              rows={2}
              value={orderNote}
              onChange={(e) => setOrderNote(e.target.value)}
              placeholder="Ghi chú cho cả đơn... (khách yêu cầu đặc biệt...)"
              className="mt-1 w-full resize-none rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)] placeholder:text-[var(--muted)]/60 focus:outline-none focus:ring-1 focus:ring-emerald-500 transition-all"
            />
          </label>

          {/* Giảm giá / Phụ thu */}
          <div className="grid grid-cols-2 gap-2">
            <label className="text-[11px] text-[var(--muted)]">
              Giảm giá
              <input
                type="number"
                className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                value={discount}
                onChange={(e) => setDiscount(Number(e.target.value) || 0)}
              />
            </label>
            <label className="text-[11px] text-[var(--muted)]">
              Phụ thu
              <input
                type="number"
                className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
                value={surcharge}
                onChange={(e) => setSurcharge(Number(e.target.value) || 0)}
              />
            </label>
          </div>

          <label className="block text-[11px] text-[var(--muted)]">
            Khách đưa
            <input
              type="number"
              className="mt-1 w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={customerPaid}
              onChange={(e) => setCustomerPaid(Number(e.target.value) || 0)}
            />
          </label>

          {/* Tổng tiền */}
          <div className="flex justify-between text-[11px] text-[var(--muted)]">
            <span>Tạm tính</span>
            <span>{subtotal.toLocaleString("vi-VN")}đ</span>
          </div>
          <div className="flex justify-between text-[11px] text-[var(--muted)]">
            <span>Giảm giá</span>
            <span>
              {discount > 0 ? `-${discount.toLocaleString("vi-VN")}đ` : "0đ"}
            </span>
          </div>
          <div className="flex justify-between text-[11px] text-[var(--muted)]">
            <span>Phụ thu</span>
            <span>
              {surcharge > 0 ? `${surcharge.toLocaleString("vi-VN")}đ` : "0đ"}
            </span>
          </div>
          <div className="mt-1 flex justify-between text-sm font-semibold text-emerald-500">
            <span>Tổng thanh toán</span>
            <span>{total.toLocaleString("vi-VN")}đ</span>
          </div>
          <div className="flex justify-between text-[11px] text-emerald-600">
            <span>Thối lại</span>
            <span>{change.toLocaleString("vi-VN")}đ</span>
          </div>

          {/* Nút chính */}
          <div className="mt-2 flex gap-2">
            {/* Thanh toán — khoá khi unsaved */}
            <button
              type="button"
              disabled={isLocked}
              title={isLocked ? "Lưu tạm đơn trước khi thanh toán" : ""}
              onClick={() => setOpenPayment(true)}
              className={`flex-1 rounded-lg py-2 text-sm font-semibold transition-all duration-200 ${
                isLocked
                  ? "cursor-not-allowed bg-slate-800 text-slate-600"
                  : "bg-emerald-500 text-slate-950 hover:bg-emerald-400"
              }`}
            >
              Thanh toán (F2)
            </button>

            {/* Lưu tạm — nổi bật khi có thay đổi */}
            <button
              type="button"
              onClick={handleDraftOrder}
              disabled={draftLoading}
              className={`relative rounded-lg border px-3 text-xs font-semibold transition-all duration-300 ${
                hasUnsaved
                  ? "animate-pulse border-amber-500 bg-amber-500/10 text-amber-400 shadow-[0_0_12px_rgba(245,158,11,0.3)]"
                  : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)]"
              } ${draftLoading ? "cursor-not-allowed opacity-60" : ""}`}
            >
              {draftLoading ? (
                <span className="flex items-center gap-1.5">
                  <span className="h-3 w-3 animate-spin rounded-full border-2 border-amber-400 border-t-transparent" />
                  Đang lưu...
                </span>
              ) : hasUnsaved ? (
                "● Lưu tạm"
              ) : (
                "Lưu tạm"
              )}
            </button>
          </div>

          {/* Hint khi có thay đổi chưa lưu */}
          {hasUnsaved && (
            <p className="animate-pulse text-center text-[10px] text-amber-500/70">
              Có thay đổi chưa lưu · Thanh toán và thao tác bàn bị tạm khoá
            </p>
          )}
        </div>
      </section>

      <PaymentModal
        open={openPayment}
        onClose={() => setOpenPayment(false)}
        total={total}
        customerPaid={customerPaid}
        onChangeCustomerPaid={setCustomerPaid}
        discount={discount}
        onChangeDiscount={setDiscount}
        surcharge={surcharge}
        onChangeSurcharge={setSurcharge}
        change={change}
      />
      <ChangeTableModal
        open={openChangeTable}
        onClose={() => setOpenChangeTable(false)}
      />
    </>
  );
}
