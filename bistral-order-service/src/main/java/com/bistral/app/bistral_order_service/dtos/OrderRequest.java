package com.bistral.app.bistral_order_service.dtos;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderRequest {
    @NotNull
    private UUID bistroId;
    @NotNull
    private UUID branchId;
    @NotNull
    @Min(0)
    private int tableNo;
    @NotEmpty
    private String orderType;

}
