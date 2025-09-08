package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.controllers.OrderItemMapper;
import com.bistral.app.bistral_order_service.dtos.*;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.entity.OrderItemEntity;
import com.bistral.app.bistral_order_service.exceptions.ResourceNotFoundException;
import com.bistral.app.bistral_order_service.mapperInterface.OrderMapper;
import com.bistral.app.bistral_order_service.openfeignclients.BistroFeignClient;
import com.bistral.app.bistral_order_service.repository.OrderItemRepository;
import com.bistral.app.bistral_order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final BistroFeignClient bistroFeignClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;


    /*
        create a new order for given bistro with branch

        @param OrderRequest which should include bistroId, branchid and other information
        @return it will return orderResponse if order created successfully
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {

        OrderEntity orderEntity = orderMapper.toOrderEntity(orderRequest);
        CompletableFuture<BranchResponse> branchResponseCompletableFuture = CompletableFuture.supplyAsync(() -> bistroFeignClient.getBranch(orderRequest.getBistroId(), orderRequest.getBranchId()));
        orderEntity.setTableNo(0);
        orderEntity.setDiscount(new BigDecimal(0));
        orderEntity.setPayableAmount(new BigDecimal(0));
        orderEntity.setTotalAmount(new BigDecimal(0));
        orderEntity.setTaxableAmount(new BigDecimal(0));
        OrderEntity finalOrderEntity1 = orderEntity;
        CompletableFuture<OrderEntity> orderEntityCompletableFuture = CompletableFuture.supplyAsync(() -> orderRepository.save(finalOrderEntity1));
        BranchResponse branchResponse = branchResponseCompletableFuture.join();
        orderEntity = orderEntityCompletableFuture.join();
        if (!orderEntity.getBranchId().equals(branchResponse.getBranchId())) {
            orderRepository.delete(orderEntity);
            throw new ResourceNotFoundException("Bistro Branch", "Invalid Bistro / Branch id");
        }
        return orderMapper.toOrderResponse(orderEntity);
    }

    @Transactional
    public OrderResponse updateOrderItemInOrder(OrderItemRequest orderItemRequest) {
        OrderEntity orderEntity = getOrderByOrderId(orderItemRequest.getOrderId());
        OrderItemEntity orderItemEntity = orderEntity.getOrderItemMap().get(orderItemRequest.getOrderItemId());
        if (orderItemEntity == null)
            throw new ResourceNotFoundException("orderItem", "order item not found with Id : " + orderItemRequest.getOrderItemId());
        orderItemEntity.setQty(new BigDecimal(orderItemRequest.getQty()));
        orderItemEntity.setPrice(orderItemEntity.getPrice());
        orderEntity.reCalcTotals();
        List<OrderItemResponse> orderItemResponseList = orderEntity.getOrderItemMap().values()
                .stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderItemResponseList);
        return orderResponse;
    }


    public OrderResponse addItemOrderInOrder(OrderItemRequest orderItemRequest) {
        CompletableFuture<MenuItemVariantResponse> menuItemVariantResponseCompletableFuture = CompletableFuture.supplyAsync(()
                -> bistroFeignClient.getItem(orderItemRequest.getMenuItemId(), orderItemRequest.getVariantId())
        );
        OrderEntity orderEntity = getOrderByOrderId(orderItemRequest.getOrderId());
        MenuItemVariantResponse menuItemVariantResponse = menuItemVariantResponseCompletableFuture.join();
        OrderItemEntity orderItemEntity = orderItemMapper.toOrderItemEntity(orderItemRequest);
        orderItemEntity.setName(menuItemVariantResponse.getItemName());
        orderItemEntity.setMenuItemId(orderItemRequest.getMenuItemId());
        orderItemEntity.setUnit(menuItemVariantResponse.getUnit());
        orderItemEntity.setTaxRate(menuItemVariantResponse.getTaxRate());
        orderItemEntity.setMenuItemId(menuItemVariantResponse.getItemId());
        orderItemEntity.setOrder(orderEntity);
        orderItemEntity.setQty(new BigDecimal(orderItemRequest.getQty()));
        orderItemEntity.setPrice(menuItemVariantResponse.getPrice());
        orderEntity.getOrderItemMap().put(orderItemEntity.getOrderItemId(),orderItemEntity);
        orderEntity.reCalcTotals();
        orderItemRepository.save(orderItemEntity);
        orderRepository.save(orderEntity);
        List<OrderItemResponse> orderItemResponseList = orderEntity.getOrderItemMap().values()
                .stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderItemResponseList);
        return orderResponse;
    }


    public OrderEntity getOrderByOrderId(UUID orderId) {
        return orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Order not found with Order Id : " + orderId));
    }
}
