package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.*;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.exceptions.CloseOrderException;
import com.bistral.app.bistral_order_service.mapperInterface.OrderItemMapper;
import com.bistral.app.bistral_order_service.mapperInterface.OrderMapper;
import com.bistral.app.bistral_order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderRequest));
    }

    @PostMapping("/add/item")
    public ResponseEntity<OrderResponse> addItem(@Valid @RequestBody OrderItemRequest orderItemRequest) throws CloseOrderException {
        return ResponseEntity.ok(orderService.addItemOrderInOrder(orderItemRequest));
    }

    @PostMapping("/add/item/bulk")
    public ResponseEntity<OrderResponse> addItems(@Valid @RequestBody BulkOrderItemRequest bulkOrderItemRequest) throws CloseOrderException {
        return ResponseEntity.ok(orderService.addBulkItemOrderInOrder(bulkOrderItemRequest));
    }

    @PatchMapping("/update/item")
    public ResponseEntity<OrderResponse> updateItem(@Valid @RequestBody OrderItemRequest orderItemRequest) throws CloseOrderException {
        return ResponseEntity.ok(orderService.updateOrderItemInOrder(orderItemRequest));
    }

    @PatchMapping("/update/item/bulk")
    public ResponseEntity<OrderResponse> updateItemInBulk(@Valid @RequestBody UpdateOrderItemRequestBulk updateOrderItemRequestBulk) throws CloseOrderException {
        return ResponseEntity.ok(orderService.updateOrderItemInOrderBulk(updateOrderItemRequestBulk));
    }

    @DeleteMapping("/{orderId}/item/{orderItemId}")
    public ResponseEntity<OrderResponse> removeItemFromOrder(@PathVariable UUID orderId, @PathVariable UUID orderItemId) {
        return ResponseEntity.ok(orderService.deleteItemFromOrder(orderId, orderItemId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        OrderEntity orderEntity = orderService.getOrderByOrderId(orderId);
        List<OrderItemResponse> itemResponseList = new ArrayList<>();
        orderEntity.getOrderItemEntityList().forEach((orderItemEntity) -> {
//            System.out.println(orderItemEntity);
            itemResponseList.add(orderItemMapper.toOrderItemResponse(orderItemEntity));
        });
        List<PaymentResponse> paymentResponseList =
                orderEntity.getPaymentEntities()
                        .stream()
                        .map(paymentEntity -> PaymentResponse.builder().paymentId(paymentEntity.getPaymentId())
                                .amount(paymentEntity.getAmount())
                                .status(paymentEntity.getPaymentStatus())
                                .paymentMode(paymentEntity.getPaymentMode())
                                .build()).toList();
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setTableId(orderEntity.getTableId());
        orderResponse.setTableNo(orderEntity.getTableNo());
        orderResponse.setPaymentStatus(orderEntity.getPaymentStatus());
        orderResponse.setOrderStatus(orderEntity.getOrderStatus());
        orderResponse.setOrderItemList(itemResponseList);
        orderResponse.setPaymentResponseList(paymentResponseList);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/branch/{branchId}/order/all")
    public List<OrderResponse> getActiveOrders(@PathVariable UUID branchId) {
        return orderService.getAllOrderOfBistro(branchId);
    }


}
