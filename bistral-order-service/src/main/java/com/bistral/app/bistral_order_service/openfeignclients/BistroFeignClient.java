package com.bistral.app.bistral_order_service.openfeignclients;

import java.util.UUID;

import com.bistral.app.bistral_order_service.config.FeignClientConfig;
import com.bistral.app.bistral_order_service.dtos.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bistral-bistro-service"
        ,
        path = "/bistros",
        configuration = {FeignClientConfig.class}
)
public interface BistroFeignClient {

    @GetMapping("/branches")
    BranchResponse getBranch();

    @GetMapping("/menus/menu-items/{itemId}/variants/{variantId}")
    MenuItemVariantResponse getItem(@PathVariable UUID itemId, @PathVariable UUID variantId);

    @Deprecated
    @PostMapping("/menus/variant/{variantId}/menu-items/{itemId}/variants/bulk")
    List<MenuItemVariantResponse> getMenuItems(@PathVariable UUID itemId, @RequestBody MenuItemVariantBulkRequest menuItemVariantBulkRequest);

    @GetMapping("/branch/table/{zoneId}")
    List<TableResponse> getTables(@PathVariable("zoneId") UUID zoneId);

    @PostMapping("/internal/menu/item/variants")
    ApiResponse<List<MenuItemVariantResponse>> getMenItemVariantsResponse(@RequestBody ItemVariantFilterDto itemVariantFilterDto);

}
