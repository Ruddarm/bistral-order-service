package com.bistral.app.bistral_order_service.openfeignclients;

import java.util.UUID;

import com.bistral.app.bistral_order_service.dtos.BranchResponse;
import com.bistral.app.bistral_order_service.dtos.MenuItemVariantBulkRequest;
import com.bistral.app.bistral_order_service.dtos.MenuItemVariantResponse;
import com.bistral.app.bistral_order_service.dtos.TableResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bistral-bistro-service"
        ,
        path = "/bistros"
)
public interface BistroFeignClient {

    @GetMapping("/{bistroId}/branches/{branchId}")
    BranchResponse getBranch(@PathVariable UUID bistroId, @PathVariable UUID branchId);

    @GetMapping("/menus/menu-items/{itemId}/variants/{variantId}")
    MenuItemVariantResponse getItem(@PathVariable UUID itemId, @PathVariable UUID variantId);

    @PostMapping("/menus/menu-items/{itemId}/variants/bulk")
    List<MenuItemVariantResponse> getMenuItems(@PathVariable UUID itemId, @RequestBody MenuItemVariantBulkRequest menuItemVariantBulkRequest);

    @GetMapping("/branch/{branchId}/table")
    List<TableResponse> getTables(@PathVariable UUID branchId);

}
