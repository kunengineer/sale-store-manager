import { useMemo, useState, useEffect } from "react";
import { getProducts } from "../data/services/productService";
import { useStore } from "../store/StoreContext";

export function ProductsPage() {
  const { currentStoreId } = useStore();
  const [products, setProducts] = useState([]);
  const [search, setSearch] = useState("");
  const [categoryFilter, setCategoryFilter] = useState("Tất cả");
  const [statusFilter, setStatusFilter] = useState("Tất cả");
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    name: "",
    category: "Cafe",
    type: "Món uống",
    price: "",
    status: "active",
  });

  useEffect(() => {
    if (currentStoreId) {
      loadProducts();
    }
  }, [currentStoreId]);

  const loadProducts = async () => {
    try {
      const res = await getProducts(currentStoreId);
      setProducts(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const filtered = useMemo(() => {
    return products.filter((p) => {
      const matchSearch = p.productName
        ?.toLowerCase()
        .includes(search.toLowerCase());

      const matchCategory =
        categoryFilter === "Tất cả" || p.categoryName === categoryFilter;

      return matchSearch && matchCategory;
    });
  }, [products, search, categoryFilter]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!form.name || !form.price) return;
    setProducts((prev) => [
      ...prev,
      {
        id: crypto.randomUUID(),
        name: form.name,
        category: form.category,
        type: form.type || "Món",
        price: Number(form.price) || 0,
        status: form.status,
      },
    ]);
    setForm({
      name: "",
      category: "Cafe",
      type: "Món uống",
      price: "",
      status: "active",
    });
    setShowForm(false);
  };

  return (
    <div className="h-[calc(100vh-3.5rem)] overflow-auto px-4 py-3">
      <div className="mb-3 flex items-center justify-between">
        <div>
          <h1 className="text-lg font-semibold text-[var(--text)]">Sản phẩm</h1>
          <p className="text-xs text-[var(--muted)]">
            Quản lý menu món, giá bán, danh mục và trạng thái kinh doanh.
          </p>
        </div>
        <button
          type="button"
          className="rounded-lg bg-emerald-500 px-3 py-1.5 text-xs font-semibold text-slate-950"
          onClick={() => setShowForm(true)}
        >
          + Sản phẩm
        </button>
      </div>

      <div className="mb-3 flex flex-wrap gap-2 text-xs">
        <input
          className="w-56 rounded-lg border border-[var(--border)] bg-[var(--input)] px-3 py-2 text-xs text-[var(--text)] placeholder:text-[var(--muted)]"
          placeholder="Tìm theo tên hoặc mã sản phẩm..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />
        <select
          className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]"
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
        >
          <option value="Tất cả">Tất cả danh mục</option>
          <option value="Cafe">Cafe</option>
          <option value="Trà">Trà</option>
        </select>
        <select
          className="rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-2 text-xs text-[var(--text)]"
          value={statusFilter}
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <option>Đang bán</option>
          <option>Ngừng bán</option>
          <option>Tất cả</option>
        </select>
      </div>

      {showForm && (
        <form
          onSubmit={handleSubmit}
          className="mb-3 grid gap-2 rounded-xl border border-[var(--border)] bg-[var(--surface-solid)] p-3 text-xs text-[var(--text)] md:grid-cols-4"
        >
          <div className="md:col-span-2">
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Tên sản phẩm
            </label>
            <input
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.name}
              onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
            />
          </div>
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Danh mục
            </label>
            <select
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              value={form.category}
              onChange={(e) =>
                setForm((f) => ({ ...f, category: e.target.value }))
              }
            >
              <option value="Cafe">Cafe</option>
              <option value="Trà">Trà</option>
              <option value="Khác">Khác</option>
            </select>
          </div>
          <div>
            <label className="mb-1 block text-[11px] text-[var(--muted)]">
              Giá bán
            </label>
            <input
              className="w-full rounded-lg border border-[var(--border)] bg-[var(--input)] px-2 py-1.5 text-xs text-[var(--text)]"
              type="number"
              value={form.price}
              onChange={(e) =>
                setForm((f) => ({ ...f, price: e.target.value }))
              }
            />
          </div>
          <div className="md:col-span-4 flex justify-end gap-2">
            <button
              type="button"
              className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-3 py-1.5 text-[11px] text-[var(--text)]"
              onClick={() => setShowForm(false)}
            >
              Hủy
            </button>
            <button
              type="submit"
              className="rounded-lg bg-emerald-500 px-3 py-1.5 text-[11px] font-semibold text-slate-950"
            >
              Lưu
            </button>
          </div>
        </form>
      )}

      <div className="overflow-hidden rounded-xl border border-[var(--border)] bg-[var(--surface-solid)]">
        <table className="min-w-full text-left text-xs text-[var(--text)]">
          <thead className="bg-[var(--surface)] text-[var(--muted)]">
            <tr>
              <th className="px-3 py-2">Sản phẩm</th>
              <th className="px-3 py-2">Danh mục</th>
              <th className="px-3 py-2 text-right">Giá bán</th>
              <th className="px-3 py-2">Trạng thái</th>
              <th className="px-3 py-2 text-right">Thao tác</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-[var(--border)]">
            {filtered.map((p) => (
              <tr key={p.productId}>
                <td className="px-3 py-2">
                  <div className="flex items-center gap-2">
                    <div className="h-8 w-8 rounded-lg bg-[var(--surface-2)]" />
                    <div>
                      <p className="text-xs font-semibold text-[var(--text)]">
                        {p.productName}
                      </p>
                      <p className="text-[11px] text-[var(--muted)]">
                        {p.type}
                      </p>
                      {/* variants */}
                      <div className="mt-1 space-y-1 text-[11px] text-[var(--muted)]">
                        {p.variants?.map((v) => (
                          <div
                            key={v.variantId}
                            className="flex justify-between"
                          >
                            <span>{v.variantName}</span>

                            <span className="text-emerald-400">
                              {v.price.toLocaleString("vi-VN")}đ
                            </span>
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                </td>
                <td className="px-3 py-2 text-xs text-[var(--text)]">
                  {p.categoryName}
                </td>
                <td className="px-3 py-2 text-right text-xs font-semibold text-emerald-400">
                  {p.variants?.[0]?.price
                    ? p.variants[0].price.toLocaleString("vi-VN") + "đ"
                    : "-"}
                </td>
                <td className="px-3 py-2">
                  <span className="rounded-full bg-emerald-500/10 px-2 py-0.5 text-[11px] text-emerald-300">
                    {p.status === "active" ? "Đang bán" : "Ngừng bán"}
                  </span>
                </td>
                <td className="px-3 py-2 text-right text-xs">
                  <button className="rounded-lg border border-[var(--border)] bg-[var(--surface-2)] px-2 py-1 text-[11px] text-[var(--text)]">
                    Sửa
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
