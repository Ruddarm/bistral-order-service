package com.bistral.app.bistral_order_service.openfeignclients;


import com.bistral.app.bistral_order_service.dtos.BranchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "bistral-bistro-service"
        ,
        path = "/bistros"
)
public interface BistroFeignClient {

    @GetMapping("/{bistroId}/branches/{branchId}")
    public BranchResponse getBranch(@PathVariable UUID bistroId, @PathVariable UUID branchId);
}
