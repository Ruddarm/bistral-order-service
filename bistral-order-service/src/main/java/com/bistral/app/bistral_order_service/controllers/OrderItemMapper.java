package com.bistral.app.bistral_order_service.controllers;

import com.bistral.app.bistral_order_service.dtos.OrderItemRequest;
import com.bistral.app.bistral_order_service.dtos.OrderItemResponse;
import com.bistral.app.bistral_order_service.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemResponse toOrderItemResponse(OrderItemEntity orderItemEntity);
    OrderItemEntity toOrderItemEntity(OrderItemRequest orderItemRequest);
}


