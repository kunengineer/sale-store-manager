
INSERT INTO stores (store_code, store_name, address, phone, email, open_time, close_time, is_active, created_at)
VALUES
('ST001', 'Cafe Trung Kiên - Quận 1', '123 Nguyễn Huệ, Q1', '0909123456', 'q1@cafe.com', '07:00:00', '23:00:00', true, NOW()),
('ST002', 'Cafe Trung Kiên - Thủ Đức', '456 Võ Văn Ngân, Thủ Đức', '0909222333', 'td@cafe.com', '07:30:00', '22:30:00', true, NOW());


INSERT INTO store_zones (store_id, zone_name, zone_type, capacity, is_active)
VALUES
-- Store 1
(2, 'Tầng Trệt', 'FLOOR', 50, true),
(2, 'Lầu 1', 'FLOOR', 40, true),
(2, 'Phòng VIP 1', 'ROOM', 12, true),
(2, 'Ban Công', 'OUTDOOR', 20, true),
(2, 'Quầy Thu Ngân', 'CASHIER', 2, true),
(2, 'Kho Nguyên Liệu', 'STORAGE', 10, true),

-- Store 2
(2, 'Tầng Trệt', 'FLOOR', 60, true),
(2, 'Lầu 1', 'FLOOR', 42, true),
(2, 'Phòng Máy Lạnh', 'ROOM', 22, true),
(2, 'Sân Thượng', 'OUTDOOR', 30, true),
(2, 'Quầy Thu Ngân', 'CASHIER', 2, true),
(2, 'Kho', 'STORAGE', 12, true);

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


INSERT INTO categories
(category_id, parent_id, category_name, image_url, sort_order, is_active, created_at, store_id)
VALUES
-- ROOT LEVEL
(1, NULL, 'Coffee', '/img/coffee.png', 1, true, NOW(), 2),
(2, NULL, 'Tea', '/img/tea.png', 2, true, NOW(), 2),
(3, NULL, 'Bakery', '/img/bakery.png', 3, true, NOW(), 2),

-- COFFEE CHILD
(6, 1, 'Milk Coffee', '/img/milkcoffee.png', 2, true, NOW(), 2),

-- TEA CHILD
(7, 2, 'Fruit Tea', '/img/fruittea.png', 1, true, NOW(), 2),
(8, 2, 'Milk Tea', '/img/milktea.png', 2, true, NOW(), 2),

-- BAKERY CHILD
(9, 3, 'Cake', '/img/cake.png', 1, true, NOW(), 2),
(10, 3, 'Bread', '/img/bread.png', 2, true, NOW(), 2),

-- TOPPING CHILD
(11, 2, 'Pearl', '/img/pearl.png', 1, true, NOW(), 2),
(12, 2, 'Cheese Foam', '/img/cheese.png', 2, true, NOW(), 2);



INSERT INTO products
(product_id, category_id, product_code, product_name, description, unit,
 base_price, cost_price, images, is_active, created_at, updated_at, store_id)
VALUES
(1, 1, 'CF001', 'Cà phê đen', 'Đen đá truyền thống', 'ly', 20000, 10000, '[]', true, NOW(), NOW(), 2),
(2, 1, 'CF002', 'Cà phê sữa', 'Sữa đá', 'ly', 25000, 12000, '[]', true, NOW(), NOW(), 2),
(3, 1, 'CF003', 'Bạc xỉu', 'Nhiều sữa ít cafe', 'ly', 28000, 14000, '[]', true, NOW(), NOW(), 2),
(6, 2, 'TE002', 'Trà chanh', 'Chanh tươi', 'ly', 22000, 9000, '[]', true, NOW(), NOW(), 2),
(7, 2, 'TE003', 'Trà sữa', 'Truyền thống', 'ly', 32000, 16000, '[]', true, NOW(), NOW(), 2),
(8, 2, 'TE004', 'Matcha', 'Matcha Nhật', 'ly', 38000, 20000, '[]', true, NOW(), NOW(), 2),
(9, 1, 'CF005', 'Americano', 'Cafe Mỹ', 'ly', 27000, 13000, '[]', true, NOW(), NOW(), 2),
(10, 1, 'CF006', 'Mocha', 'Cafe socola', 'ly', 40000, 22000, '[]', true, NOW(), NOW(), 2),
(11, 2, 'TE005', 'Trà vải', 'Vải tươi', 'ly', 31000, 15000, '[]', true, NOW(), NOW(), 2),
(12, 2, 'TE006', 'Trà dâu', 'Dâu tươi', 'ly', 33000, 17000, '[]', true, NOW(), NOW(), 2);





INSERT INTO product_variants
(variant_id, product_id, variant_type, variant_name, sku, barcode,
 attributes, price, cost_price, is_active)
VALUES
(1, 1, 'BASE', 'M', 'CF001-M', '111', '{}', 20000, 10000, true),
(2, 1, 'BASE', 'L', 'CF001-L', '112', '{}', 25000, 12000, true),

(3, 2, 'BASE', 'M', 'CF002-M', '113', '{}', 25000, 12000, true),

(6, 3, 'BASE', 'L', 'CF003-L', '116', '{}', 33000, 17000, true),

(7, 2, 'BASE', 'M', 'CF004-M', '117', '{}', 35000, 18000, true),
(8, 2, 'BASE', 'L', 'CF004-L', '118', '{}', 40000, 20000, true),

(9, 2, 'BASE', 'M', 'TE001-M', '119', '{}', 30000, 15000, true),
(10, 2, 'BASE', 'L', 'TE001-L', '120', '{}', 35000, 18000, true),

(11, 6, 'BASE', 'M', 'TE002-M', '121', '{}', 22000, 9000, true),
(12, 6, 'BASE', 'L', 'TE002-L', '122', '{}', 27000, 12000, true),

(20, 1, 'TOPPING', 'Trân châu', 'TOP-001', '200', '{}', 5000, 2000, true),
(21, 1, 'TOPPING', 'Cheese Foam', 'TOP-002', '201', '{}', 7000, 3000, true);


INSERT INTO store_variant_prices
(store_variant_price_id, store_id, variant_id, price, is_active)
VALUES
(1, 2, 1, 21000, true),
(2, 2, 2, 26000, true),
(3, 2, 3, 26000, true),
(6, 2, 6, 34000, true);