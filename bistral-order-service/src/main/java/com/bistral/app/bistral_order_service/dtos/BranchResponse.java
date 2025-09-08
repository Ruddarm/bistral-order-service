package com.bistral.app.bistral_order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchResponse {
    private UUID branchId;
    private String branchName;
    private int tables;
    private String Address;

}
