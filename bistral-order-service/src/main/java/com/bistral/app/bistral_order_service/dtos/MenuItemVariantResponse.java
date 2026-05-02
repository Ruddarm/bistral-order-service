package com.bistral.app.bistral_order_service.dtos;

import com.bistral.app.bistral_order_service.enums.ItemUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemVariantResponse {
    private UUID itemId;
    private String itemName;
    private String variantName;
    private UUID variantId;
    private BigDecimal price;
    private BigDecimal taxRate;
    private boolean isTaxIncluded;
    private BigDecimal qty;
    private ItemUnit unit;

}
