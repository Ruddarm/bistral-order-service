package com.bistral.app.bistral_order_service.service;

import com.bistral.app.bistral_order_service.mapperInterface.OrderItemMapper;
import com.bistral.app.bistral_order_service.dtos.*;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.entity.OrderItemEntity;
import com.bistral.app.bistral_order_service.entity.enums.OrderStatus;
import com.bistral.app.bistral_order_service.exceptions.ResourceNotFoundException;
import com.bistral.app.bistral_order_service.mapperInterface.OrderMapper;
import com.bistral.app.bistral_order_service.openfeignclients.BistroFeignClient;
import com.bistral.app.bistral_order_service.repository.OrderItemRepository;
import com.bistral.app.bistral_order_service.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final BistroFeignClient bistroFeignClient;
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final ConcurrentHashMap<UUID, OrderEntity> activeOrderMap = new ConcurrentHashMap<>();

    /*
        create a new order for given bistro with branch
        @param OrderRequest which should include bistroId, branchid and other information
        @return it will return orderResponse if order created successfully
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {

        OrderEntity orderEntity = orderMapper.toOrderEntity(orderRequest);
        CompletableFuture<BranchResponse> branchResponseCompletableFuture = CompletableFuture.supplyAsync(() -> bistroFeignClient.getBranch(orderRequest.getBistroId(), orderRequest.getBranchId()));
        orderEntity.setTableNo(orderRequest.getTableNo());
        orderEntity.setTableId(orderRequest.getTableId());
        orderEntity.setDiscount(new BigDecimal(0));
        orderEntity.setPayableAmount(new BigDecimal(0));
        orderEntity.setTaxAmount(new BigDecimal(0));
        orderEntity.setTaxableAmount(new BigDecimal(0));
        orderEntity.setOrderStatus(OrderStatus.Open);
        OrderEntity finalOrderEntity1 = orderEntity;
        CompletableFuture<OrderEntity> orderEntityCompletableFuture = CompletableFuture.supplyAsync(() -> orderRepository.save(finalOrderEntity1));
        BranchResponse branchResponse = branchResponseCompletableFuture.join();
        orderEntity = orderEntityCompletableFuture.join();
        orderEntity.setOrderItemEntityList(new ArrayList<>());
        activeOrderMap.put(orderEntity.getOrderId(), orderEntity);
        if (!orderEntity.getBranchId().equals(branchResponse.getBranchId())) {
            orderRepository.delete(orderEntity);
            throw new ResourceNotFoundException("Bistro Branch", "Invalid Bistro / Branch id");
        }
        return orderMapper.toOrderResponse(orderEntity);
    }

    @Transactional
    public OrderResponse updateOrderItemInOrder(OrderItemRequest orderItemRequest) {
        OrderEntity orderEntity = getOrderByOrderId(orderItemRequest.getOrderId());
        OrderItemEntity orderItemEntity = orderEntity
                .getOrderItemEntityList()
                .stream()
                .filter((oi) -> oi.getOrderItemId() == orderItemRequest.getOrderItemId())
                .findFirst()
                .orElse(null);
        if (orderItemEntity == null)
            throw new ResourceNotFoundException("orderItem", "order item not found with Id : " + orderItemRequest.getOrderItemId());
        orderItemEntity.setOrderedQty(new BigDecimal(orderItemRequest.getOrderedQty()));
        orderItemEntity.setPrice(orderItemEntity.getPrice());
        orderEntity.reCalcTotals();
        List<OrderItemResponse> orderItemResponseList = orderEntity.getOrderItemEntityList()
                .stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderItemResponseList);
        return orderResponse;
    }

    @Transactional
    public OrderResponse updateOrderItemInOrderBulk(UpdateOrderItemRequestBulk updateOrderItemRequestBulk) {
        OrderEntity orderEntity = getOrderByOrderId(updateOrderItemRequestBulk.orderId());
        Map<UUID, OrderItemEntity> orderItemEntityMap = orderEntity.getOrderItemEntityList()
                .stream()
                .collect(Collectors.toMap(OrderItemEntity::getOrderItemId, o -> o));
        updateOrderItemRequestBulk.items().
                forEach(updateOrderItemRequest -> {
                    OrderItemEntity orderItemEntity = orderItemEntityMap.getOrDefault(updateOrderItemRequest.orderItemId(), null);
                    if (orderItemEntity == null)
                        throw new ResourceNotFoundException("orderItem", "Order item not found with Id " + updateOrderItemRequest.orderItemId());
                    orderItemEntity.setOrderedQty(new BigDecimal(updateOrderItemRequest.orderedQty()));
                });
        orderEntity.reCalcTotals();
        List<OrderItemResponse> orderItemResponseList = orderEntity.getOrderItemEntityList()
                .stream()
                .map(orderItemMapper::toOrderItemResponse).toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderItemResponseList);
        activeOrderMap.put(orderEntity.getOrderId(), orderEntity);
        return orderResponse;
    }

    /*
        Add single item in order
     */
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
        orderItemEntity.setOrderedQty(new BigDecimal(orderItemRequest.getOrderedQty()));
        orderItemEntity.setPrice(menuItemVariantResponse.getPrice());
        orderEntity.getOrderItemEntityList().add(orderItemEntity);
        orderEntity.reCalcTotals();
        orderItemRepository.save(orderItemEntity);
        orderRepository.save(orderEntity);
        List<OrderItemResponse> orderItemResponseList = orderEntity.getOrderItemEntityList()
                .stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderItemResponseList);
        return orderResponse;
    }

    /*
        Add item in bulk in order
     */
    public OrderResponse addBulkItemOrderInOrder(BulkOrderItemRequest bulkOrderItemRequest) {
        List<UUID> variantIds = bulkOrderItemRequest.items().stream()
                .map(OrderItemRequest::getVariantId).toList();
        MenuItemVariantBulkRequest menuItemVariantBulkRequest = new MenuItemVariantBulkRequest(bulkOrderItemRequest.items().getFirst().getMenuItemId(), variantIds);
        CompletableFuture<List<MenuItemVariantResponse>> futureMenus = CompletableFuture
                .supplyAsync(() -> bistroFeignClient.getMenuItems(menuItemVariantBulkRequest.menuItemId(), menuItemVariantBulkRequest));
        OrderEntity orderEntity = getOrderByOrderId(bulkOrderItemRequest.orderId());
        Map<UUID, MenuItemVariantResponse> itemVariantResponses = futureMenus.join().stream().collect(Collectors.toMap(MenuItemVariantResponse::getVariantId, item -> item, (a, b) -> a));
        List<OrderItemEntity> newOrderItem = new ArrayList<>();
        for (int i = 0; i < bulkOrderItemRequest.items().size(); i++) {
            OrderItemRequest orderItemRequest = bulkOrderItemRequest.items().get(i);
            if (!itemVariantResponses.containsKey(orderItemRequest.getVariantId()))
                throw new ResourceNotFoundException("ItemVariant", "Variant not found with " + orderItemRequest.getVariantId());
            MenuItemVariantResponse menuItemVariantResponse = itemVariantResponses.get(orderItemRequest.getVariantId());
            OrderItemEntity orderItemEntity = orderItemMapper.toOrderItemEntity(orderItemRequest);
            orderItemEntity.setName(menuItemVariantResponse.getItemName());
            orderItemEntity.setMenuItemId(menuItemVariantResponse.getItemId());
            orderItemEntity.setUnit(menuItemVariantResponse.getUnit());
            orderItemEntity.setTaxRate(menuItemVariantResponse.getTaxRate());
            orderItemEntity.setOrderedQty(BigDecimal.valueOf(orderItemRequest.getOrderedQty()));
            orderItemEntity.setPrice(menuItemVariantResponse.getPrice());
            orderItemEntity.setOrder(orderEntity);
            newOrderItem.add(orderItemEntity);
        }
        List<OrderItemEntity> orderItemEntities = orderItemRepository.saveAll(newOrderItem);
        orderItemEntities.forEach(orderItemEntity -> orderEntity.getOrderItemEntityList().add(orderItemEntity));
        orderEntity.reCalcTotals();
        orderRepository.updateTotals(orderEntity.getOrderId(), orderEntity.getTaxableAmount(), orderEntity.getTaxAmount(), orderEntity.getPayableAmount());
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(orderEntity.getOrderItemEntityList().stream().map(orderItemMapper::toOrderItemResponse).toList());
        return orderResponse;
    }

    //Remove Item from the given order
    @Transactional()
    public OrderResponse deleteItemFromOrder(UUID orderId, UUID orderItemId) {
        OrderEntity orderEntity = getOrderByOrderId(orderId);
        orderEntity.getOrderItemEntityList().removeIf((orderItemEntity -> orderItemEntity.getOrderItemId().equals(orderItemId)));
        orderEntity.reCalcTotals();
        OrderEntity order = orderRepository.save(orderEntity);
        activeOrderMap.put(orderId,order);
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setOrderItemList(order.getOrderItemEntityList().stream().map(orderItemMapper::toOrderItemResponse).toList());
        return orderResponse;
    }

    public OrderEntity getOrderByOrderId(UUID orderId) {
        if (activeOrderMap.containsKey(orderId)) return activeOrderMap.get(orderId);
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Order not found with Order Id : " + orderId));
        activeOrderMap.put(orderEntity.getOrderId(), orderEntity);
        return orderEntity;
    }

    public List<OrderResponse> getAllOrderOfBistro(UUID branchId) {
        CompletableFuture<List<TableResponse>> listCompletableFuture = CompletableFuture.supplyAsync(() -> bistroFeignClient.getTables(branchId));
        List<OrderEntity> orderEntityList = orderRepository.findByBranchIdAndOrderStatus(branchId, OrderStatus.Open);
        Map<UUID, OrderEntity> orderMap = orderEntityList.stream().collect(Collectors.toMap(OrderEntity::getTableId, o -> o));
        return listCompletableFuture.join().stream()
                .map(tableResponse -> {
                    OrderEntity orderEntity = orderMap.get(tableResponse.getTableId());
                    if (orderEntity == null) {
                        return OrderResponse.builder()
                                .tableId(tableResponse.getTableId())
                                .tableNo(tableResponse.getTableNo())
                                .taxAmount(new BigDecimal(0))
                                .taxableAmount(new BigDecimal(0))
                                .branchId(branchId)
                                .discount(BigDecimal.ZERO)
                                .orderStatus(OrderStatus.Vacant)
                                .orderItemList(new ArrayList<>())
                                .build();
                    } else {
                        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
                        orderResponse.setTableId(tableResponse.getTableId());
                        orderResponse.setTableNo(tableResponse.getTableNo());
                        orderResponse.setOrderStatus(orderEntity.getOrderStatus());
                        List<OrderItemResponse> orderItemResponses = orderEntity.getOrderItemEntityList().stream()
                                .map(orderItemMapper::toOrderItemResponse)
                                .toList();
                        orderResponse.setOrderItemList(orderItemResponses);
                        return orderResponse;
                    }
                })
                .toList();
    }

}
