package com.be.ssm.service.impl.saleImpl;

import com.be.ssm.dto.request.sale.OrderItemCreateRequest;
import com.be.ssm.dto.request.sale.OrderItemUpdateRequest;
import com.be.ssm.dto.response.sale.OrderItemResponse;
import com.be.ssm.entities.product.ProductVariants;
import com.be.ssm.entities.sales.OrderItems;
import com.be.ssm.entities.sales.Orders;
import com.be.ssm.exceptions.CustomException;
import com.be.ssm.exceptions.Error;
import com.be.ssm.helper.OrderTotalCalculator;
import com.be.ssm.entities.store.StoreProductPrice;
import com.be.ssm.entities.store.StoreVariantPrice;
import com.be.ssm.mapper.sales.OrderItemMapper;
import com.be.ssm.repository.product.ProductVariantsRepository;
import com.be.ssm.repository.sales.OrderItemsRepository;
import com.be.ssm.repository.sales.OrdersRepository;
import com.be.ssm.repository.store.StoreProductPriceRepository;
import com.be.ssm.repository.store.StoreVariantPriceRepository;
import com.be.ssm.service.sale.OrderItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemsRepository repository;
    private final OrdersRepository ordersRepository;
    private final ProductVariantsRepository productVariantsRepository;

    private final OrderTotalCalculator orderTotalCalculator;

    private final OrderItemMapper mapper;
    // Order service
    private final StoreVariantPriceRepository storeVariantPriceRepository;
    private final StoreProductPriceRepository storeProductPriceRepository;

    @Override
    public OrderItemResponse getById(Integer id) {
        log.info("Getting order item by id {}", id);

        return mapper.toOrderItemResponse(findById(id));
    }

    @Override
    public List<OrderItems> buildItems(List<OrderItemCreateRequest> itemRequests, Orders order, Integer storeId) {
        return itemRequests.stream()
                .map(req -> buildOrderItem(order, storeId, req))
                .toList();
    }

    private OrderItems buildOrderItem(Orders order, Integer storeId, OrderItemCreateRequest request) {
        ProductVariants variant = findProductVariantById(request.getProductVariantId());

        BigDecimal unitPrice = resolveUnitPrice(storeId, variant);
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        OrderItems item = mapper.toOrderItemEntity(request);
        item.setOrder(order);
        item.setProductVariants(variant);
        item.setUnitPrice(unitPrice);
        item.setLineTotal(lineTotal);

        return item;
    }

    @Override
    public OrderItemResponse update(OrderItemUpdateRequest request, Integer id) {
        log.info("Update order item with id {}", id);

        OrderItems orderItem = findById(id);
        Integer storeId = orderItem.getOrder().getStoreTables().getZone().getStore().getStoreId();

        BigDecimal unitPrice = resolveUnitPrice(storeId, orderItem.getProductVariants());
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));
        orderItem.setUnitPrice(unitPrice);
        orderItem.setLineTotal(lineTotal);

        mapper.updateEntityFromRequest(request, orderItem);

        repository.save(orderItem);

        Orders order = findOrderById(request.getOrderId());
        orderTotalCalculator.recalculate(order);
        Orders saved = ordersRepository.save(order);

        OrderItems savedItem = saved.getOrderItems()
                .getLast();

        return mapper.toOrderItemResponse(savedItem);
    }

    @Override
    public void delete(List<Integer> orderItem) {
        List<OrderItems> items = orderItem
                .stream()
                .map(this::findById)
                .toList();
        Integer orderId = items.getFirst().getOrder().getOrderId();

        repository.deleteAll(items);

        Orders order = findOrderById(orderId);
        orderTotalCalculator.recalculate(order);
        ordersRepository.save(order);
    }

    private OrderItems findById(Integer id) {
        log.info("Finding order item by id {}", id);

        return repository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ORDER_ITEM_NOT_FOUND));
    }

    private ProductVariants findProductVariantById(Integer id) {
        log.info("Finding product variant by id {}", id);

        return productVariantsRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.PRODUCT_VARIANT_NOT_FOUND));
    }

    private Orders findOrderById(Integer id) {
        log.info("Finding orders by id {}", id);

        return ordersRepository.findById(id)
                .orElseThrow(()-> new CustomException(Error.ORDER_NOT_FOUND));
    }

    private BigDecimal resolveUnitPrice(Integer storeId, ProductVariants variant) {

        Integer variantId = variant.getVariantId();
        Integer productId = variant.getProduct().getProductId();

        // 1️⃣ Giá variant theo store
        var storeVariantPrice = storeVariantPriceRepository
                .findActivePrice(storeId, variantId)
                .map(StoreVariantPrice::getPrice);

        if (storeVariantPrice.isPresent()) {
            return storeVariantPrice.get();
        }

        // 2️⃣ Giá product theo store
        var storeProductPrice = storeProductPriceRepository
                .findActivePrice(storeId, productId)
                .map(StoreProductPrice::getPrice);

        if (storeProductPrice.isPresent()) {
            return storeProductPrice.get();
        }

        // 3️⃣ Giá mặc định của variant
        if (variant.getPrice() != null) {
            return variant.getPrice();
        }

        // 4️⃣ Giá mặc định của product
        if (variant.getProduct().getBasePrice() != null) {
            return variant.getProduct().getBasePrice();
        }

        // 5️⃣ fallback cuối
        return BigDecimal.ZERO;
    }

}
