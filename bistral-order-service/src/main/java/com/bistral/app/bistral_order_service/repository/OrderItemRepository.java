package com.bistral.app.bistral_order_service.repository;


import com.bistral.app.bistral_order_service.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

}
