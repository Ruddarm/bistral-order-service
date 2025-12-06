package com.bistral.app.bistral_order_service.repository;


import com.bistral.app.bistral_order_service.entity.OrderNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderNumberRepository extends JpaRepository<OrderNumberEntity, UUID> {


}
