package com.bistral.app.bistral_order_service.dtos;


import brave.internal.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemRequest {
    @NotNull
    private UUID orderId;
    @NotNull
    private UUID menuId;
    @NotNull
    private UUID menuItemId;
    @NotNull
    private UUID variantId;

}
