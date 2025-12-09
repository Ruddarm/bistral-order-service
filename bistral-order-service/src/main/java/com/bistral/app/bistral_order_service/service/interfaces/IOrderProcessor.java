package com.bistral.app.bistral_order_service.service.interfaces;

import com.bistral.app.bistral_order_service.dtos.BulkOrderItemRequest;
import com.bistral.app.bistral_order_service.dtos.OrderItemRequest;
import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import com.bistral.app.bistral_order_service.dtos.UpdateOrderItemRequestBulk;
import com.bistral.app.bistral_order_service.entity.OrderEntity;

import java.util.UUID;

public interface IOrderProcessor {

    OrderResponse createOrder();
    OrderResponse addItemInOrder(OrderItemRequest orderItemRequest);
    OrderResponse addItemInOrder(BulkOrderItemRequest bulkOrderItemRequest);
    OrderResponse updateItemInOrder(OrderItemRequest orderItemRequest);
    OrderResponse updateItemInOrderBulk(UpdateOrderItemRequestBulk updateOrderItemRequestBulk);
    OrderResponse deleteItemInOrder(UUID orderId,UUID orderItemId);
    OrderResponse saveOrder(OrderEntity orderEntity);

}
