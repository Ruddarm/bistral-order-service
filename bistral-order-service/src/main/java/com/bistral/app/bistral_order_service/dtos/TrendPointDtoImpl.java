package com.bistral.app.bistral_order_service.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrendPointDtoImpl implements  TrendPointDto {
    private  String label;
    private  double value;
    @Override
    public String getLabel() {
        return this.label;
    }

    @Override
    public double getValue() {
        return this.value;
    }
}
