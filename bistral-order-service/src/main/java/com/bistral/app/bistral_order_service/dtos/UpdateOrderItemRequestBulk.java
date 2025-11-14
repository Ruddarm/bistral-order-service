package com.bistral.app.bistral_order_service.dtos;

import java.util.List;
import java.util.UUID;

public record UpdateOrderItemRequestBulk(UUID orderId, List<UpdateOrderItemRequest> items) {

}
