package com.bistral.app.bistral_order_service.entity;


import jakarta.persistence.*;

import java.util.UUID;
@Entity
public class OrderNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public  UUID orderNumberId;
    public  UUID bistroId;
    public  UUID branchId;
    public int orderNumber;
}
