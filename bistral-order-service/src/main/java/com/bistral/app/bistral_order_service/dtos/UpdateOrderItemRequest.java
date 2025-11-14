package com.bistral.app.bistral_order_service.dtos;

import java.util.UUID;

public record UpdateOrderItemRequest(UUID orderItemId, int orderedQty) {
}
