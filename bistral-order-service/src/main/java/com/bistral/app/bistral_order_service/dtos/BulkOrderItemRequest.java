package com.bistral.app.bistral_order_service.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record BulkOrderItemRequest(@NotNull UUID orderId, @NotNull List<OrderItemRequest> items) {

}
