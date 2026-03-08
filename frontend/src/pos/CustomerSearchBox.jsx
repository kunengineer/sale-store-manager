import { useState } from "react";
import { useQuery, useMutation } from "@tanstack/react-query";
import { searchCustomersPOS, createCustomer } from "../data/services/customerService";
import { useStore } from "../store/StoreContext";

export default function CustomerSearchBox({ onSelect }) {
  const { currentStoreId } = useStore();

  const [keyword, setKeyword] = useState("");
  const [newName, setNewName] = useState("");

  const { data } = useQuery({
    queryKey: ["customers", keyword, currentStoreId],
    queryFn: () =>
      searchCustomersPOS({
        keyword,
        storeId: currentStoreId,
      }),
    enabled: keyword.length >= 3,
  });

  const customers = data?.data ?? [];
  console.log("Search customers:", customers);

  const createMutation = useMutation({
    mutationFn: createCustomer,
    onSuccess: (res) => {
      onSelect(res.data.data);
      setKeyword("");
      setNewName("");
    },
  });

  const handleCreate = () => {
    createMutation.mutate({
      fullName: newName || "Khách lẻ",
      phone: keyword,
      storeId: currentStoreId,
    });
  };

  return (
    <div className="space-y-2">
      <input
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
        placeholder="Tìm khách theo SĐT hoặc tên..."
        className="w-full rounded-lg border px-3 py-2 text-sm"
      />

      {customers.map((c) => (
        <div
          key={c.customerId}
          onClick={() => onSelect(c)}
          className="cursor-pointer rounded-lg border px-3 py-2 hover:bg-emerald-50"
        >
          <div className="font-medium">{c.fullName}</div>
          <div className="text-xs text-gray-500">{c.phone}</div>
        </div>
      ))}

      {keyword.length >= 3 && customers.length === 0 && (
        <div className="rounded-lg border p-2">
          <div className="text-xs text-gray-500 mb-1">Không tìm thấy khách</div>

          <input
            placeholder="Nhập tên khách hàng..."
            value={newName}
            onChange={(e) => setNewName(e.target.value)}
            className="w-full rounded border px-2 py-1 text-sm"
          />

          <button
            onClick={handleCreate}
            className="mt-2 w-full rounded bg-emerald-600 py-1.5 text-white text-sm"
          >
            Tạo mới ({keyword})
          </button>
        </div>
      )}
    </div>
  );
}
