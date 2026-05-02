package com.bistral.app.bistral_order_service.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResponse<T> {
    private  T data;
    private  int size;
    private  int crnPage;
    private  long totalPage;
    private  long totalRecords;
    private  boolean hasNext;
    private  boolean hasPrevious;
}
