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
    public OrderItemResponse create(OrderItemCreateRequest request) {
        log.info("Create new order item");
        ProductVariants productVariants = findProductVariantById(request.getProductVariantId());
        Orders order = findOrderById(request.getOrderId());

        Integer storeId = order.getStoreTables().getZone().getStore().getStoreId();


        BigDecimal unitPrice = resolveUnitPrice(storeId, productVariants);
        BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));


        OrderItems orderItem = mapper.toOrderItemEntity(request);
        orderItem.setProductVariants(productVariants);
        orderItem.setOrder(order);
        orderItem.setUnitPrice(unitPrice);
        orderItem.setLineTotal(lineTotal);

        repository.save(orderItem);

        orderTotalCalculator.recalculate(order);

        Orders saved = ordersRepository.save(order);

        OrderItems savedItem = saved.getOrderItems()
                .getLast();

        return mapper.toOrderItemResponse(savedItem);
    }

    @Override
    public OrderItemResponse update(OrderItemUpdateRequest request, Integer id) {
        log.info("Update order item with id {}", id);

        OrderItems orderItem = findById(id);
        ProductVariants productVariants = findProductVariantById(request.getProductVariantId());
        Orders order = findOrderById(request.getOrderId());

        mapper.updateEntityFromRequest(request, orderItem);
        orderItem.setOrder(order);
        orderItem.setProductVariants(productVariants);

        repository.save(orderItem);

        orderTotalCalculator.recalculate(order);
        Orders saved = ordersRepository.save(order);

        OrderItems savedItem = saved.getOrderItems()
                .getLast();

        return mapper.toOrderItemResponse(savedItem);
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
