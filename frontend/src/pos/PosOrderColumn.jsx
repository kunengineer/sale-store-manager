import { useMemo, useState, useRef, useEffect } from "react";
import { usePos } from "./PosContext";
import { useStore } from "../store/StoreContext";
import { createOrder, addItemsToOrder, updateOrderItem, deleteOrderItems } from "../data/services/orderService";
import CustomerSearchBox from "./CustomerSearchBox";

// ─────────────────────────────────────────────────────────────
// HELPERS
// ─────────────────────────────────────────────────────────────
const parseMoney = (value) => {
  const raw = String(value).replace(/\D/g, "");
  return Math.max(0, Math.min(1_000_000_000, Number(raw) || 0));
};
const fmtVND = (n) => Number(n ?? 0).toLocaleString("vi-VN");

// ─────────────────────────────────────────────────────────────
// PAYMENT MODAL
// Chứa toàn bộ: ghi chú, giảm giá, phụ thu, khách đưa,
// thối lại, quick-cash, phương thức thanh toán.
// ─────────────────────────────────────────────────────────────
function PaymentModal({ open, onClose, subtotal, orderNote, onChangeOrderNote }) {
  const [discount,      setDiscount]      = useState(0);
  const [surcharge,     setSurcharge]     = useState(0);
  const [customerPaid,  setCustomerPaid]  = useState(0);
  const [paymentMethod, setPaymentMethod] = useState("Tiền mặt");
  const [printAfter,    setPrintAfter]    = useState(false);

  // Reset mỗi lần mở
  useEffect(() => {
    if (open) {
      setDiscount(0); setSurcharge(0);
      setCustomerPaid(0); setPaymentMethod("Tiền mặt");
    }
  }, [open]);

  const total  = useMemo(() => Math.max(0, subtotal - discount + surcharge), [subtotal, discount, surcharge]);
  const change = useMemo(() => Math.max(0, customerPaid - total), [customerPaid, total]);
  const canPay = customerPaid >= total && total > 0;

  if (!open) return null;

  const numInput = (value, onChange) => ({
    type: "text", inputMode: "numeric",
    value: value ? fmtVND(value) : "",
    onFocus: (e) => e.target.select(),
    onKeyDown: (e) => { if (e.key === "-" || e.key === "e") e.preventDefault(); },
    onChange: (e) => onChange(parseMoney(e.target.value)),
    className: `w-full rounded-lg border border-[var(--border)] bg-[var(--surface-2)]
      px-3 py-2 text-sm text-right font-bold text-[var(--text)]
      focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all`,
  });

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm px-3">
      <div className="w-full max-w-md rounded-2xl border border-[var(--border)] bg-[var(--surface-solid)] shadow-2xl text-[var(--text)]">

        {/* Header */}
        <div className="flex items-start justify-between border-b border-[var(--border)] px-4 py-3">
          <div>
            <h2 className="text-sm font-bold">Thanh toán hóa đơn</h2>
            <p className="text-xs text-[var(--muted)]">
              Tổng cần thu:{" "}
              <span className="font-bold text-emerald-500">{fmtVND(total)}đ</span>
            </p>
          </div>
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-3 py-1 text-xs text-[var(--muted)]
              hover:border-rose-400 hover:text-rose-400 transition-all">
            Đóng
          </button>
        </div>

        <div className="p-4 space-y-3">

          {/* Ghi chú đơn */}
          <div>
            <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">Ghi chú đơn hàng</label>
            <textarea rows={2} value={orderNote}
              onChange={(e) => onChangeOrderNote(e.target.value)}
              placeholder="Khách yêu cầu đặc biệt..."
              className="w-full resize-none rounded-lg border border-[var(--border)] bg-[var(--input)]
                px-2.5 py-1.5 text-xs text-[var(--text)] placeholder:text-[var(--muted)]/60
                focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all" />
          </div>

          {/* Giảm giá + Phụ thu */}
          <div className="grid grid-cols-2 gap-2">
            <div>
              <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">Giảm giá</label>
              <input {...numInput(discount, setDiscount)} placeholder="0đ" />
            </div>
            <div>
              <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">Phụ thu</label>
              <input {...numInput(surcharge, setSurcharge)} placeholder="0đ" />
            </div>
          </div>

          {/* Tiền khách đưa */}
          <div>
            <label className="mb-1 block text-[11px] font-semibold text-[var(--muted)]">Số tiền khách đưa</label>
            <input
              autoFocus type="text" inputMode="numeric"
              value={customerPaid ? fmtVND(customerPaid) : ""}
              placeholder="Nhập số tiền..."
              onFocus={(e) => e.target.select()}
              onChange={(e) => setCustomerPaid(parseMoney(e.target.value))}
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--surface-2)]
                px-3 py-2.5 text-base text-right font-bold text-[var(--text)]
                focus:outline-none focus:ring-2 focus:ring-emerald-500/30 focus:border-emerald-500 transition-all" />
          </div>

          {/* Quick cash */}
          <div className="flex flex-wrap gap-1.5">
            <button type="button" onClick={() => setCustomerPaid(total)}
              className="rounded-full border border-emerald-500 bg-emerald-500/10 px-3 py-1
                text-[11px] font-semibold text-emerald-500 hover:bg-emerald-500/20 transition-all">
              Exact
            </button>
            {[50_000, 100_000, 200_000, 500_000].map((amt) => (
              <button key={amt} type="button"
                onClick={() => setCustomerPaid((p) => Math.min(1_000_000_000, p + amt))}
                className="rounded-full border border-[var(--border)] bg-[var(--surface-2)]
                  px-3 py-1 text-[11px] font-medium hover:border-emerald-500 hover:bg-emerald-500/10 transition-all">
                {fmtVND(amt)}
              </button>
            ))}
            <button type="button" onClick={() => setCustomerPaid(0)}
              className="rounded-full border border-rose-400/50 px-3 py-1 text-[11px]
                font-medium text-rose-400 hover:bg-rose-500/10 transition-all">
              Clear
            </button>
          </div>

          {/* Tổng kết */}
          <div className="rounded-xl border border-[var(--border)] bg-[var(--surface-2)] p-3 space-y-1.5 text-xs">
            {[
              { label: "Tạm tính",       val: `${fmtVND(subtotal)}đ`,     cls: "" },
              { label: "Giảm giá",       val: `−${fmtVND(discount)}đ`,    cls: "" },
              { label: "Phụ thu",        val: `+${fmtVND(surcharge)}đ`,   cls: "" },
              { label: "Tổng thanh toán",val: `${fmtVND(total)}đ`,        cls: "font-bold text-emerald-500 text-sm" },
              { label: "Tiền khách đưa", val: `${fmtVND(customerPaid)}đ`, cls: "" },
              { label: "Tiền thừa",      val: `${fmtVND(change)}đ`,       cls: "font-bold text-emerald-500" },
            ].map((r) => (
              <div key={r.label} className="flex items-center justify-between">
                <span className="text-[var(--muted)]">{r.label}</span>
                <span className={r.cls || "text-[var(--text)]"}>{r.val}</span>
              </div>
            ))}
          </div>

          {/* Phương thức thanh toán */}
          <div>
            <p className="mb-1.5 text-[11px] font-semibold text-[var(--muted)]">Phương thức thanh toán</p>
            <div className="flex flex-wrap gap-1.5">
              {["Tiền mặt", "Thẻ", "Chuyển khoản", "Ví điện tử"].map((m) => (
                <button key={m} type="button" onClick={() => setPaymentMethod(m)}
                  className={`rounded-full border px-3 py-1 text-[11px] font-medium transition-all
                    ${paymentMethod === m
                      ? "border-emerald-500 bg-emerald-500/15 text-emerald-500"
                      : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)] hover:border-emerald-500"}`}>
                  {m}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="flex items-center justify-between border-t border-[var(--border)] px-4 py-3">
          <label className="flex cursor-pointer items-center gap-2 text-xs text-[var(--muted)]">
            <input type="checkbox" checked={printAfter}
              onChange={(e) => setPrintAfter(Boolean(e.target.checked))}
              className="h-3.5 w-3.5 accent-emerald-500" />
            In hóa đơn sau khi thanh toán
          </label>
          <div className="flex gap-2">
            <button type="button" disabled={!canPay}
              className={`rounded-lg px-5 py-2 text-xs font-bold transition-all
                ${canPay
                  ? "bg-emerald-600 text-white hover:bg-emerald-700 shadow-sm shadow-emerald-500/30"
                  : "cursor-not-allowed bg-[var(--surface-2)] text-[var(--muted)]"}`}>
              Thanh toán
            </button>
            <button type="button" disabled={!canPay}
              className={`rounded-lg px-5 py-2 text-xs font-bold transition-all
                ${canPay
                  ? "bg-emerald-700 text-white hover:bg-emerald-800"
                  : "cursor-not-allowed bg-[var(--surface-2)] text-[var(--muted)]"}`}>
              Thanh toán & In
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────
// CHANGE TABLE MODAL
// ─────────────────────────────────────────────────────────────
function ChangeTableModal({ open, onClose }) {
  const { tables, selectedTable, selectTable } = usePos();
  if (!open) return null;
  return (
    <div className="fixed inset-0 z-40 flex items-center justify-center bg-black/60">
      <div className="w-full max-w-sm rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3 text-xs text-[var(--text)]">
        <div className="mb-2 flex items-center justify-between">
          <p className="text-sm font-bold">Đổi bàn</p>
          <button type="button" onClick={onClose}
            className="rounded-lg border border-[var(--border)] px-2.5 py-1 text-[11px]
              text-[var(--muted)] hover:border-rose-400 transition-all">
            Đóng
          </button>
        </div>
        <p className="mb-2 text-[11px] text-[var(--muted)]">Chọn bàn muốn chuyển hoá đơn hiện tại sang.</p>
        <div className="grid grid-cols-3 gap-2">
          {tables.map((t) => (
            <button key={t.id} type="button"
              onClick={() => { selectTable(t.id); onClose(); }}
              className={`rounded-lg border px-2 py-1.5 text-left text-[11px] transition-all
                ${selectedTable?.id === t.id
                  ? "border-emerald-500 bg-emerald-500/10"
                  : "border-[var(--border)] bg-[var(--surface-2)] hover:border-emerald-500/50"}`}>
              <span className="block font-semibold">{t.name}</span>
              <span className="block text-[10px] text-[var(--muted)]">
                {t.status === "using" ? `${t.guests ?? 0} khách` : t.status === "reserved" ? "Đã đặt" : "Trống"}
              </span>
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}

// ─────────────────────────────────────────────────────────────
// POS ORDER COLUMN
//

// ─────────────────────────────────────────────────────────────
// SNAPSHOT HELPERS
// Dùng để diff trạng thái items giữa 2 lần lưu tạm.
// snapshot shape: { [item.id]: { orderItemId, qty, note } }
// ─────────────────────────────────────────────────────────────

/** Tạo snapshot từ danh sách items hiện tại */
function buildSnapshot(items) {
  return Object.fromEntries(
    items.map((i) => [i.id, { orderItemId: i.orderItemId ?? null, qty: i.qty, note: i.note ?? "" }])
  );
}

/** So sánh 2 snapshot — true nếu bằng nhau */
function snapshotEqual(a, b) {
  const keysA = Object.keys(a);
  const keysB = Object.keys(b);
  if (keysA.length !== keysB.length) return false;
  return keysA.every((k) => b[k] && b[k].qty === a[k].qty && b[k].note === a[k].note);
}

// ─────────────────────────────────────────────────────────────
// POS ORDER COLUMN
//
// Layout (flex-col, h-full):
//   ┌─ Header       (shrink-0) — tên bàn, đổi bàn
//   ├─ Customer     (shrink-0)
//   ├─ Order items  (flex-1, overflow-y-auto) ← SCROLL ở đây
//   └─ Footer       (shrink-0) — Lưu tạm, Thanh toán (luôn hiện)
//
// Draft save logic — 4 trường hợp:
//   [1] Item mới + chưa có order       → createOrder(toàn bộ items)
//   [2] Item mới + đã có order         → addItemsToOrder(orderId, newItems)
//   [3] Item cũ bị đổi qty/note        → updateOrderItem(itemId, payload)
//   [4] Item cũ bị xóa khỏi list       → deleteOrderItem(itemId)
// ─────────────────────────────────────────────────────────────
export function PosOrderColumn() {
  const [openPayment,     setOpenPayment]     = useState(false);
  const [openChangeTable, setOpenChangeTable] = useState(false);

  const {
    selectedTable, orderItems, changeItemQty, removeItem,
    subtotal, currentOrderId, setCurrentOrderId, updateItemNote,
  } = usePos();
  const { theme } = useStore();

  const [hasUnsaved,   setHasUnsaved]   = useState(false);
  const [draftLoading, setDraftLoading] = useState(false);
  const [draftError,   setDraftError]   = useState(null);

  /**
   * lastSavedSnapshot: lưu trạng thái orderItems tại thời điểm lưu tạm thành công.
   * Dùng để diff xem item nào mới / bị đổi qty-note / bị xóa.
   * Dạng: { [item.id]: { orderItemId, qty, note } }
   */
  const lastSavedSnapshot = useRef(buildSnapshot(orderItems));

  /**
   * skipNextUnsavedCheck – giải quyết race condition khi đổi bàn:
   *
   * Timeline lỗi (KHÔNG có flag):
   *   1. selectedTable.id đổi → useEffect chạy → snapshot = {} (items chưa load)
   *   2. selectTable async load BE xong → setOrderItems([item1, item2])
   *   3. useEffect([orderItems]) chạy → so sánh {item1,item2} vs {} → hasUnsaved=true ❌
   *
   * Fix (CÓ flag):
   *   1. selectedTable.id đổi → set flag = true, hasUnsaved = false
   *   2. setOrderItems([item1, item2])
   *   3. useEffect([orderItems]) thấy flag=true → sync snapshot + clear flag ✓
   *   4. User chỉnh item → useEffect([orderItems]) flag=false → compare bình thường ✓
   */
  const skipNextUnsavedCheck = useRef(false);

  const [customer,  setCustomer]  = useState(null);
  const [orderNote, setOrderNote] = useState("");

  // Khi đổi bàn: đặt flag bỏ qua lần load items từ BE
  useEffect(() => {
    skipNextUnsavedCheck.current = true;
    setHasUnsaved(false);
  }, [selectedTable?.id]);

  // Theo dõi orderItems:
  //  - Lần đầu sau đổi bàn (flag=true): sync snapshot, clear flag → hasUnsaved=false
  //  - Các lần sau (user chỉnh sửa): compare bình thường
  useEffect(() => {
    if (skipNextUnsavedCheck.current) {
      lastSavedSnapshot.current = buildSnapshot(orderItems);
      skipNextUnsavedCheck.current = false;
      setHasUnsaved(false);
      return;
    }
    setHasUnsaved(!snapshotEqual(buildSnapshot(orderItems), lastSavedSnapshot.current));
  }, [orderItems]);

  // isLocked: khoá Thanh toán / Gộp bàn / Chuyển bàn khi có thay đổi chưa lưu
  const isLocked = hasUnsaved;

  const tableTimeText = useMemo(() => {
    if (!selectedTable) return null;
    const src = selectedTable.status === "using" ? selectedTable.openedAt
              : selectedTable.status === "reserved" ? selectedTable.reservedAt : null;
    if (!src) return null;
    const d = new Date(src);
    return `${d.toLocaleDateString("vi-VN")} · ${d.toLocaleTimeString("vi-VN", { hour: "2-digit", minute: "2-digit" })}`;
  }, [selectedTable]);

  // ── HANDLE DRAFT ────────────────────────────────────────────
  const handleDraftOrder = async () => {
    if (!hasUnsaved || draftLoading) return;
    if (!selectedTable) { window.alert("Vui lòng chọn bàn trước khi lưu tạm."); return; }

    setDraftLoading(true);
    setDraftError(null);

    try {
      const prevSnap = lastSavedSnapshot.current; // snapshot lần lưu trước

      // ── Phân loại items ──────────────────────────────────
      // [A] Items KHÔNG có orderItemId → chưa tồn tại trên BE (item mới thêm)
      const brandNewItems = orderItems.filter((i) => !i.orderItemId);

      // [B] Items CÓ orderItemId → đã tồn tại trên BE
      const existingItems = orderItems.filter((i) => !!i.orderItemId);

      // [C] Items cũ bị XÓA = có trong prevSnap nhưng không còn trong orderItems
      const currentOrderItemIds = new Set(existingItems.map((i) => i.orderItemId));
      const deletedOrderItemIds = Object.values(prevSnap)
        .filter((s) => s.orderItemId && !currentOrderItemIds.has(s.orderItemId))
        .map((s) => s.orderItemId);

      // [D] Items cũ bị ĐỔI qty hoặc note
      const changedItems = existingItems.filter((i) => {
        const prev = Object.values(prevSnap).find((s) => s.orderItemId === i.orderItemId);
        if (!prev) return false; // không có trong snap → mới thêm, không phải changed
        return prev.qty !== i.qty || prev.note !== (i.note ?? "");
      });

      // ── Gọi API theo thứ tự ─────────────────────────────
      if (!currentOrderId) {
        // [1] Chưa có order → tạo mới, gửi toàn bộ items (cả brandNew và existing nếu có)
        const payload = {
          tableId:    selectedTable.id,
          customerId: customer?.customerId ?? null,
          note:       orderNote,
          subtotal,
          items: orderItems.map((i) => ({
            productVariantId: i.base?.variantId,
            quantity:         i.qty,
            discountPct:      0,
            note:             i.note ?? "",
          })),
        };
        const res = await createOrder(payload);
        // BE trả về OrderResponse, lấy orderId để lưu vào context
        const newOrderId = res?.data?.orderId ?? res?.orderId;
        if (newOrderId) setCurrentOrderId(newOrderId);

      } else {
        // Đã có order — xử lý song song các thay đổi

        // [2] Xóa items bị remove
        // Gửi 1 request duy nhất — BE nhận @RequestBody List<Integer>
        if (deletedOrderItemIds.length > 0) {
          await deleteOrderItems(deletedOrderItemIds);
        }

        // [3] Update items đổi qty/note
        if (changedItems.length > 0) {
          await Promise.all(
            changedItems.map((i) =>
              updateOrderItem(i.orderItemId, {
                orderId:          currentOrderId,
                productVariantId: i.base?.variantId,
                quantity:         i.qty,
                discountPct:      0,
                note:             i.note ?? "",
              })
            )
          );
        }

        // [4] Thêm items mới vào order đã có
        if (brandNewItems.length > 0) {
          await addItemsToOrder(
            currentOrderId,
            brandNewItems.map((i) => ({
              productVariantId: i.base?.variantId,
              quantity:         i.qty,
              discountPct:      0,
              note:             i.note ?? "",
            }))
          );
        }
      }

      // Cập nhật snapshot sau khi lưu thành công
      lastSavedSnapshot.current = buildSnapshot(orderItems);
      setHasUnsaved(false);

    } catch (err) {
      console.error("Lưu tạm thất bại:", err);
      setDraftError(err?.message ?? "Lưu tạm thất bại. Vui lòng thử lại.");
    } finally {
      setDraftLoading(false);
    }
  };

  return (
    <>
      <section className={`flex h-full w-[28%] min-w-[260px] max-w-[360px] flex-col rounded-xl border
        ${theme === "dark"
          ? "border-[var(--border)] bg-[var(--surface)]"
          : "border-[var(--border)] bg-[var(--surface-solid)]"}`}>

        {/* ── HEADER ── */}
        <div className="flex shrink-0 items-start justify-between border-b border-[var(--border)] px-3 py-2">
          <div>
            <div className="flex items-center gap-2">
              <p className="text-sm font-semibold text-[var(--text)]">
                {selectedTable ? `Bàn ${selectedTable.name}` : "Chưa chọn bàn"}
              </p>
              {tableTimeText && (
                <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[10px] text-emerald-600">
                  {selectedTable.status === "using" ? "Mở lúc" : "Đặt lúc"} {tableTimeText}
                </span>
              )}
            </div>
            <p className="text-xs text-emerald-600">
              {selectedTable
                ? selectedTable.status === "using"
                  ? `Đang phục vụ · ${selectedTable.guests ?? 0} khách`
                  : selectedTable.status === "reserved" ? "Đã đặt chỗ" : "Trống"
                : "Chọn bàn để tạo hóa đơn"}
            </p>
          </div>
          <button type="button" disabled={isLocked}
            title={isLocked ? "Lưu tạm đơn trước khi đổi bàn" : ""}
            onClick={() => setOpenChangeTable(true)}
            className={`rounded-lg border px-3 py-1.5 text-[11px] transition-all
              ${isLocked
                ? "cursor-not-allowed border-[var(--border)] text-[var(--muted)]/30"
                : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)] hover:border-emerald-500"}`}>
            Đổi bàn
          </button>
        </div>

        {/* ── CUSTOMER ── */}
        <div className="shrink-0">
          {customer ? (
            <div className="mx-3 mt-2 flex items-center justify-between rounded-lg border
              border-emerald-500/30 bg-emerald-500/5 px-3 py-2 text-xs">
              <div>
                <div className="font-semibold text-[var(--text)]">{customer.fullName}</div>
                <div className="text-[10px] text-[var(--muted)]">{customer.phone}</div>
              </div>
              <button type="button" onClick={() => setCustomer(null)}
                className="rounded border border-rose-400/40 px-2 py-0.5 text-[10px]
                  text-rose-400 hover:bg-rose-500/10 transition-all">✕</button>
            </div>
          ) : (
            <CustomerSearchBox onSelect={setCustomer} />
          )}
        </div>

        {/* ── ORDER ITEMS ── flex-1 + overflow-y-auto = scroll khi nhiều món */}
        <div className="flex-1 overflow-y-auto border-t border-[var(--border)] p-2 space-y-1.5">
          {orderItems.length === 0 ? (
            <div className="flex flex-col items-center justify-center gap-2 py-10 text-center">
              <span className="text-3xl opacity-20">🧾</span>
              <p className="text-xs text-[var(--muted)]/60">Chưa có món nào</p>
            </div>
          ) : (
            orderItems.map((item) => (
              <div key={item.id}
                className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-2.5 py-2">
                <div className="flex items-start gap-2">
                  {/* Tên + topping + note */}
                  <div className="min-w-0 flex-1">
                    <p className="text-xs font-semibold text-[var(--text)]">
                      {item.productName}
                      {item.base?.variantName && (
                        <span className="ml-1 font-normal text-[var(--muted)]">· {item.base.variantName}</span>
                      )}
                    </p>
                    {item.toppings?.map((t) => (
                      <p key={t.variantId} className="ml-1 text-[10px] text-[var(--muted)]">+ {t.variantName}</p>
                    ))}
                    <input
                      type="text" value={item.note ?? ""}
                      onChange={(e) => updateItemNote(item.id, e.target.value)}
                      placeholder="Ghi chú... (ít đá, không đường...)"
                      className="mt-1 w-full rounded border border-transparent bg-transparent px-1 py-0.5
                        text-[10px] text-[var(--muted)] placeholder:text-[var(--muted)]/40
                        hover:border-[var(--border)] focus:outline-none focus:border-emerald-500/50 transition-colors"
                    />
                  </div>

                  {/* Giá + qty stepper + nút xóa */}
                  <div className="flex shrink-0 flex-col items-end gap-1.5">
                    <div className="flex items-center gap-1">
                      <p className="text-xs font-bold text-[var(--text)]">
                        {(item.price * item.qty).toLocaleString("vi-VN")}đ
                      </p>
                      {/* Nút xóa item */}
                      <button
                        type="button"
                        onClick={() => removeItem(item.id)}
                        title="Xóa món"
                        className="flex h-4 w-4 items-center justify-center rounded-full
                          text-[var(--muted)]/40 hover:bg-rose-500/15 hover:text-rose-500
                          transition-colors text-[10px] leading-none"
                      >
                        ✕
                      </button>
                    </div>
                    {/* Qty stepper */}
                    <div className="inline-flex items-center gap-1.5 rounded-full border
                      border-[var(--border)] bg-[var(--surface)] px-2 py-0.5 text-[10px]">
                      <button type="button"
                        className="flex h-3.5 w-3.5 items-center justify-center rounded-full
                          hover:bg-rose-500/20 hover:text-rose-500 transition-colors"
                        onClick={() => changeItemQty(item.id, -1)}>−</button>
                      <span className="min-w-[14px] text-center font-semibold text-[var(--text)]">{item.qty}</span>
                      <button type="button"
                        className="flex h-3.5 w-3.5 items-center justify-center rounded-full
                          hover:bg-emerald-500/20 hover:text-emerald-500 transition-colors"
                        onClick={() => changeItemQty(item.id, 1)}>+</button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {/* ── FOOTER ── shrink-0, luôn dính đáy */}
        <div className="shrink-0 space-y-2 border-t border-[var(--border)] bg-[var(--surface)] p-2.5">

          {/* Tạm tính */}
          <div className="flex items-center justify-between rounded-lg border border-[var(--border)]
            bg-[var(--surface-2)] px-3 py-1.5 text-xs">
            <span className="text-[var(--muted)]">Tạm tính</span>
            <span className="font-bold text-[var(--text)]">{subtotal.toLocaleString("vi-VN")}đ</span>
          </div>

          {/* Draft error */}
          {draftError && (
            <p className="rounded-lg bg-rose-500/10 px-2 py-1.5 text-[10px] font-medium text-rose-500">
              ⚠ {draftError}
            </p>
          )}

          {/* Nút phụ */}
          <div className="flex gap-1.5 text-[10px]">
            {[
              { label: "Gộp bàn",    lock: true,  act: () => window.alert("Gộp bàn: sẽ xử lý sau."), hover: "hover:border-amber-500 hover:text-amber-500" },
              { label: "Chuyển bàn", lock: true,  act: () => setOpenChangeTable(true), hover: "hover:border-amber-500 hover:text-amber-500" },
              { label: "Hủy đơn",   lock: false, act: () => window.alert("Hủy đơn: sẽ xử lý sau."), hover: "hover:border-rose-500 hover:text-rose-500" },
            ].map(({ label, lock, act, hover }) => (
              <button key={label} type="button"
                disabled={lock && isLocked}
                title={lock && isLocked ? "Lưu tạm đơn trước" : ""}
                onClick={act}
                className={`flex-1 rounded-lg border py-1.5 transition-all
                  ${lock && isLocked
                    ? "cursor-not-allowed border-[var(--border)] text-[var(--muted)]/30"
                    : `border-[var(--border)] text-[var(--text)] ${hover}`}`}>
                {label}
              </button>
            ))}
          </div>

          {/* Nút chính */}
          <div className="flex gap-2">
            <button type="button"
              disabled={isLocked}
              title={isLocked ? "Lưu tạm đơn trước khi thanh toán" : ""}
              onClick={() => setOpenPayment(true)}
              className={`flex-1 rounded-lg py-2.5 text-sm font-bold transition-all
                ${isLocked
                  ? "cursor-not-allowed bg-[var(--surface-2)] text-[var(--muted)]/40"
                  : "bg-emerald-600 text-white hover:bg-emerald-700 shadow-sm shadow-emerald-500/20"}`}>
              Thanh toán
            </button>

            <button type="button" onClick={handleDraftOrder} disabled={draftLoading}
              className={`rounded-lg border px-3 text-xs font-semibold transition-all duration-300
                ${hasUnsaved
                  ? theme === "dark"
                    ? "animate-pulse border-amber-500 bg-amber-500/10 text-amber-400"
                    : "border-amber-500 bg-amber-100 text-amber-700 shadow-sm"
                  : "border-[var(--border)] bg-[var(--surface-2)] text-[var(--text)]"}
                ${draftLoading ? "cursor-not-allowed opacity-60" : ""}`}>
              {draftLoading ? (
                <span className="flex items-center gap-1.5">
                  <span className="h-3 w-3 animate-spin rounded-full border-2 border-amber-400 border-t-transparent" />
                  Đang lưu...
                </span>
              ) : hasUnsaved ? "● Lưu tạm" : "Lưu tạm"}
            </button>
          </div>

          {hasUnsaved && !draftError && (
            <p className="animate-pulse text-center text-[10px] text-amber-500/70">
              Có thay đổi chưa lưu · Thanh toán và thao tác bàn bị tạm khoá
            </p>
          )}
        </div>
      </section>

      <PaymentModal
        open={openPayment}
        onClose={() => setOpenPayment(false)}
        subtotal={subtotal}
        orderNote={orderNote}
        onChangeOrderNote={setOrderNote}
      />
      <ChangeTableModal
        open={openChangeTable}
        onClose={() => setOpenChangeTable(false)}
      />
    </>
  );
}