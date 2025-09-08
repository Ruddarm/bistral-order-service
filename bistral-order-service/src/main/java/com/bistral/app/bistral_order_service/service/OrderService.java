package com.bistral.app.bistral_order_service.service;


import com.bistral.app.bistral_order_service.dtos.BranchResponse;
import com.bistral.app.bistral_order_service.dtos.OrderRequest;
import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.exceptions.ResourceNotFoundException;
import com.bistral.app.bistral_order_service.openfeignclients.BistroFeignClient;
import com.bistral.app.bistral_order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final ModelMapper modelMapper;
    private final BistroFeignClient bistroFeignClient;
    private final OrderRepository orderRepository;

    /*
        create a new order for given bistro with branch

        @param OrderRequest which should include bistroId, branchid and other information
        @return it will return orderResponse if order created successfully
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {
        OrderEntity orderEntity = modelMapper.map(orderRequest, OrderEntity.class);
        BranchResponse branchResponse = bistroFeignClient.getBranch(orderRequest.getBistroId(), orderRequest.getBranchId());
        orderEntity.setTableNo(0);
        orderEntity.setDiscount(new BigDecimal(0));
        orderEntity.setPayableAmount(new BigDecimal(0));
        orderEntity.setTotalAmount(new BigDecimal(0));
        orderEntity.setTaxableAmount(new BigDecimal(0));
        orderRepository.save(orderEntity);
        return modelMapper.map(orderEntity, OrderResponse.class);
    }

    public OrderEntity getOrderByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "Order not found with Order Id : " + orderId));
    }
}
