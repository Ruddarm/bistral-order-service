package com.bistral.app.bistral_order_service.mapperInterface;

import com.bistral.app.bistral_order_service.dtos.OrderRequest;
import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderEntity toOrderEntity(OrderRequest orderRequest);
    OrderResponse toOrderResponse(OrderEntity orderEntity);
}
