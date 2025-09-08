//package com.bistral.app.bistral_order_service.service;
//
//import com.bistral.app.bistral_order_service.dtos.MenuItemVariantResponse;
//import com.bistral.app.bistral_order_service.dtos.OrderItemRequest;
//import com.bistral.app.bistral_order_service.dtos.OrderResponse;
//import com.bistral.app.bistral_order_service.entity.OrderEntity;
//import com.bistral.app.bistral_order_service.entity.OrderItemEntity;
//import com.bistral.app.bistral_order_service.openfeignclients.BistroFeignClient;
//import com.bistral.app.bistral_order_service.repository.OrderItemRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//
//@RequiredArgsConstructor
//@Service
//public class OrderItemService {
//
//    private final ModelMapper modelMapper;
//    private final OrderItemRepository orderItemRepository;
//    private final OrderService orderService;
//    private final BistroFeignClient bistroFeignClient;
//
//
//    @Transactional
//    public OrderResponse createOrderItem(OrderItemRequest orderItemRequest) {
//        OrderEntity orderEntity = orderService.getOrderByOrderId(orderItemRequest.getOrderId());
//        MenuItemVariantResponse menuItemVariantResponse = bistroFeignClient.getItem(orderItemRequest.getMenuItemId(), orderItemRequest.getVariantId());
//        OrderItemEntity orderItemEntity = modelMapper.map(orderItemRequest, OrderItemEntity.class);
//        orderItemEntity.setName(menuItemVariantResponse.getItemName());
//        orderItemEntity.setMenuItemId(orderItemRequest.getMenuItemId());
//        orderItemEntity.setQty(new BigDecimal(orderItemRequest.getQty()));
//        orderItemEntity.setUnit(menuItemVariantResponse.getUnit());
//        orderItemEntity.setPrice(menuItemVariantResponse.getPrice().multiply(orderItemEntity.getQty()));
//        orderItemEntity.setTaxRate(menuItemVariantResponse.getTaxRate());
//        orderItemEntity.setMenuItemId(menuItemVariantResponse.getItemId());
//        orderItemEntity.setOrder(orderEntity);
//        orderEntity.getOrderItemSet().add(orderItemEntity);
//        orderEntity.updateOrderAmounts(orderItemEntity);
//        return modelMapper.map(orderEntity, OrderResponse.class);
//    }
//
//
//
//}
