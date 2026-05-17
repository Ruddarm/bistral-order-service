package com.bistral.app.bistral_order_service.controllers;


import com.bistral.app.bistral_order_service.dtos.OrderItemRequest;
import com.bistral.app.bistral_order_service.dtos.OrderItemResponse;
import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orders/items")
public class OrderItemsController {


    @PostMapping()
    public OrderResponse addItems(List<OrderItemRequest> orderItemRequests){
        return null;
    }
}
