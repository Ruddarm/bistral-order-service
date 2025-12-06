package com.bistral.app.bistral_order_service.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Table(
        uniqueConstraints =
                {@UniqueConstraint(name = "bistroBranch", columnNames = {"bistroId", "branchId"})}
)
@Entity(name = "order_number")
public class OrderNumberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID orderNumberId;
    public UUID bistroId;
    public UUID branchId;
    public int orderNumber;
}
