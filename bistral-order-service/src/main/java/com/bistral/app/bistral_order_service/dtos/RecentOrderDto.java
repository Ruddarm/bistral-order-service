package com.bistral.app.bistral_order_service.dtos;

public interface RecentOrderDto {
    int getOrderNumber();
    int getTableNumber();
    int getItemCount();
    int getPayableAmount();
}
