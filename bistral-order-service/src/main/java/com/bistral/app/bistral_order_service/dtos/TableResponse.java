package com.bistral.app.bistral_order_service.dtos;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TableResponse {
    @EqualsAndHashCode.Include
    private  UUID tableId;
    private int tableNo;
}
