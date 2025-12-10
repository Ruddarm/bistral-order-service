package com.bistral.app.bistral_order_service.dtos;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SqlResultSetMapping(
        name = "TrendPointMapping",
        classes = @ConstructorResult(
                targetClass = TrendPointDtoImpl.class,
                columns = {
                        @ColumnResult(name = "label", type = String.class),
                        @ColumnResult(name = "value", type = Double.class)
                }
        )
)
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
