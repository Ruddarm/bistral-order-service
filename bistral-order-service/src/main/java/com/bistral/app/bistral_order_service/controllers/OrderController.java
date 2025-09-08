package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.OrderItemRequest;
import com.bistral.app.bistral_order_service.dtos.OrderItemResponse;
import com.bistral.app.bistral_order_service.dtos.OrderRequest;
import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
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
    public ResponseEntity<OrderResponse> addItem(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.addItemOrderInOrder(orderItemRequest));
    }

    @PatchMapping("/update/item")
    public ResponseEntity<OrderResponse> updateItem(@Valid @RequestBody OrderItemRequest orderItemRequest) {
        return ResponseEntity.ok(orderService.updateOrderItemInOrder(orderItemRequest));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable UUID orderId) {
        OrderEntity orderEntity = orderService.getOrderByOrderId(orderId);
        List<OrderItemResponse> itemResponseList = new ArrayList<>();
        orderEntity.getOrderItemMap().values().forEach((orderItemEntity) -> {
            System.out.println(orderItemEntity);
            itemResponseList.add(orderItemMapper.toOrderItemResponse(orderItemEntity));
        });
        OrderResponse orderResponse = orderMapper.toOrderResponse(orderEntity);
        orderResponse.setOrderItemList(itemResponseList);
        return ResponseEntity.ok(orderResponse);
    }


}
