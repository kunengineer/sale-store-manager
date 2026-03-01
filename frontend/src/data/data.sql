INSERT INTO stores (store_code, store_name, address, phone, email, open_time, close_time, is_active, created_at)
VALUES
('ST001', 'Cafe Trung Kiên - Quận 1', '123 Nguyễn Huệ, Q1', '0909123456', 'q1@cafe.com', '07:00:00', '23:00:00', true, NOW()),
('ST002', 'Cafe Trung Kiên - Thủ Đức', '456 Võ Văn Ngân, Thủ Đức', '0909222333', 'td@cafe.com', '07:30:00', '22:30:00', true, NOW());


INSERT INTO store_zones (store_id, zone_name, zone_type, capacity, is_active)
VALUES
-- Store 1
(4, 'Tầng Trệt', 'FLOOR', 50, true),
(4, 'Lầu 1', 'FLOOR', 40, true),
(4, 'Phòng VIP 1', 'ROOM', 15, true),
(4, 'Ban Công', 'OUTDOOR', 20, true),
(4, 'Quầy Thu Ngân', 'CASHIER', 5, true),
(4, 'Kho Nguyên Liệu', 'STORAGE', 10, true),

-- Store 2
(5, 'Tầng Trệt', 'FLOOR', 60, true),
(5, 'Lầu 1', 'FLOOR', 45, true),
(5, 'Phòng Máy Lạnh', 'ROOM', 25, true),
(5, 'Sân Thượng', 'OUTDOOR', 30, true),
(5, 'Quầy Thu Ngân', 'CASHIER', 5, true),
(5, 'Kho', 'STORAGE', 12, true);

INSERT INTO store_tables (zone_id, table_code, seats, status, merged_into_table_id, is_active)
SELECT z.zone_id, t.table_code, t.seats, 'AVAILABLE', NULL, true
FROM store_zones z
JOIN stores s ON s.store_id = z.store_id
JOIN (
    -- ===== STORE ST001 =====
    SELECT 'ST001' AS store_code, 'Tầng Trệt' AS zone_name, 'A1' AS table_code, 4 AS seats
    UNION ALL SELECT 'ST001', 'Tầng Trệt', 'A2', 4
    UNION ALL SELECT 'ST001', 'Tầng Trệt', 'A3', 2
    UNION ALL SELECT 'ST001', 'Tầng Trệt', 'A4', 6
    UNION ALL SELECT 'ST001', 'Tầng Trệt', 'A5', 4

    UNION ALL SELECT 'ST001', 'Lầu 1', 'B1', 4
    UNION ALL SELECT 'ST001', 'Lầu 1', 'B2', 4
    UNION ALL SELECT 'ST001', 'Lầu 1', 'B3', 2
    UNION ALL SELECT 'ST001', 'Lầu 1', 'B4', 6
    UNION ALL SELECT 'ST001', 'Lầu 1', 'B5', 4

    UNION ALL SELECT 'ST001', 'Phòng VIP 1', 'VIP1', 8
    UNION ALL SELECT 'ST001', 'Phòng VIP 1', 'VIP2', 10

    UNION ALL SELECT 'ST001', 'Ban Công', 'BC1', 4
    UNION ALL SELECT 'ST001', 'Ban Công', 'BC2', 4

    -- ===== STORE ST002 =====
    UNION ALL SELECT 'ST002', 'Tầng Trệt', 'A1', 4
    UNION ALL SELECT 'ST002', 'Tầng Trệt', 'A2', 4
    UNION ALL SELECT 'ST002', 'Tầng Trệt', 'A3', 6
    UNION ALL SELECT 'ST002', 'Tầng Trệt', 'A4', 2
    UNION ALL SELECT 'ST002', 'Tầng Trệt', 'A5', 4

    UNION ALL SELECT 'ST002', 'Lầu 1', 'B1', 4
    UNION ALL SELECT 'ST002', 'Lầu 1', 'B2', 6
    UNION ALL SELECT 'ST002', 'Lầu 1', 'B3', 4
    UNION ALL SELECT 'ST002', 'Lầu 1', 'B4', 2
    UNION ALL SELECT 'ST002', 'Lầu 1', 'B5', 4

    UNION ALL SELECT 'ST002', 'Phòng Máy Lạnh', 'ML1', 4
    UNION ALL SELECT 'ST002', 'Phòng Máy Lạnh', 'ML2', 6

    UNION ALL SELECT 'ST002', 'Sân Thượng', 'ST1', 4
    UNION ALL SELECT 'ST002', 'Sân Thượng', 'ST2', 4
) t
ON s.store_code = t.store_code AND z.zone_name = t.zone_name;