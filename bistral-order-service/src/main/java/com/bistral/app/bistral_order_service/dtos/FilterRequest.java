package com.bistral.app.bistral_order_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterRequest {
    List<String> bistroIds;
    List<String> branchIds;
    int minPayableAmount = 0;
    int maxPayableAmount = 0;
}
