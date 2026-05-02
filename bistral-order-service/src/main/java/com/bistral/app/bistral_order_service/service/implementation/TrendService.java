package com.bistral.app.bistral_order_service.service.implementation;

import com.bistral.app.bistral_order_service.dtos.*;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.repository.OrderAnalysisRepository;
import com.bistral.app.bistral_order_service.repository.implementation.OrderAnalysis;
import com.bistral.app.bistral_order_service.service.interfaces.ITrendAnalysis;
import com.bistral.app.bistral_order_service.utils.ExcelBuilder;
import com.bistral.app.bistral_order_service.utils.Range;
import com.bistral.app.bistral_order_service.utils.RangeResolver;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class TrendService {

    private final OrderAnalysis orderAnalysis;
    private final OrderAnalysisRepository orderAnalysisRepository;
    private final RangeResolver rangeResolver;
    private final TrendGroupingResolver trendGroupingResolver;
    private final TrendFiller trendFiller;

    public List<TrendPointDto> getOrderTrend(Range range,
                                             List<UUID> bistroIds,
                                             List<UUID> branchIds) {

        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = rangeResolver.getEnd(range);

        String groupBy = switch (range) {
            case TODAY -> "hour";
            case LAST_7_DAYS -> "day";
            case WEEK, THIS_MONTH -> "week";
            default -> "day";
        };

        ITrendAnalysis grouping = trendGroupingResolver.getGrouping(groupBy);
        if (grouping == null) throw new IllegalStateException("No grouping strategy for: " + groupBy);

        List<TrendPointDtoImpl> raw = orderAnalysis.getOrderTrend(grouping.getOrderTrendQuery(bistroIds), start, end);

        return trendFiller.fillAndFormat(start, end, grouping, raw);
    }

    public List<TrendPointDto> getRevenueTrend(Range range, List<UUID> bistroIds, List<UUID> branchIds) {
        LocalDateTime start = rangeResolver.getStart(range);
        LocalDateTime end = rangeResolver.getEnd(range);
        String groupBy = switch (range) {
            case TODAY -> "hour";
            case LAST_7_DAYS -> "day";
            case WEEK, THIS_MONTH -> "week";
            default -> "day";
        };
        ITrendAnalysis grouping = trendGroupingResolver.getGrouping(groupBy);
        if (grouping == null) throw new IllegalStateException("No grouping strategy for: " + groupBy);
        List<TrendPointDtoImpl> raw = orderAnalysis.getRevenueTrend(grouping.getRevenueTrendQuery(bistroIds), start, end);
        return trendFiller.fillAndFormat(start, end, grouping, raw);
    }

    public List<TrendPointDto> getPaymentModeDistrubtion(List<UUID> bistroIds, List<UUID> branchId, Range range) {
        return orderAnalysisRepository.getPaymentModeDistribution(bistroIds, rangeResolver.getStart(range), rangeResolver.getEnd(range));
    }

    public PageResponse<List<OrderResponse>> filterdOrder(List<UUID> bistroIds, List<UUID> branchIds, BigDecimal minPayableAmount, BigDecimal maxPayableAmount, LocalDate from, LocalDate to, int page, int size) {
        PageResponse<List<OrderEntity>> pageResponse = orderAnalysis.orderResponsesFilterd(bistroIds,
                branchIds, minPayableAmount,
                maxPayableAmount, from, to, page, size);
        List<OrderResponse> orderResponses = pageResponse.getData().stream().map(orderEntity -> {
            return OrderResponse.builder()
                    .orderId(orderEntity.getOrderId())
                    .payableAmount(orderEntity.getPayableAmount())
                    .taxAmount(orderEntity.getTaxAmount())
                    .bistroId(orderEntity.getBistroId())
                    .branchId(orderEntity.getBranchId())
                    .orderStatus(orderEntity.getOrderStatus())
                    .build();
        }).toList();
        return PageResponse.<List<OrderResponse>>builder()
                .data(orderResponses)
                .hasPrevious(pageResponse.isHasPrevious())
                .totalRecords(pageResponse.getTotalRecords())
                .totalPage(pageResponse.getTotalPage())
                .crnPage(pageResponse.getCrnPage())
                .size(pageResponse.getSize())
                .hasNext(pageResponse.isHasNext())
                .build();
    }

    public Workbook GetExcelReport(ColumnRequest columnRequest) {
        List<OrderEntity> orderEntityList = orderAnalysisRepository.findAll();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);          // Bold text
        headerFont.setFontHeightInPoints((short) 14); // Font size increase
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        SXSSFSheet orderSheet = workbook.createSheet("orderSheet");
        orderSheet.trackAllColumnsForAutoSizing();
        ExcelBuilder<OrderEntity> excelBuilder = new ExcelBuilder<>(orderSheet);
        excelBuilder.writeHeader(columnRequest.getColumns(), headerStyle);
        Map<String, Function<OrderEntity, Object>> extractor = this.orderEntityColumnMap();
        orderEntityList.forEach(o -> excelBuilder.writeRow(o, columnRequest.getColumns(), extractor));
        excelBuilder.autoSizeColumn(columnRequest.getColumns().size());
        return workbook;
    }

    private Map<String, Function<OrderEntity, Object>> orderEntityColumnMap() {
        return Map.of(
                "orderId", o -> o.getOrderId().toString(),
                "bistroId", o -> o.getBistroId().toString(),
                "branchId", o -> o.getBranchId().toString(),
                "payableAmount", o -> o.getPayableAmount().toString(),
                "discount", o -> o.getDiscount().toString(),
                "taxableAmount", o -> o.getTaxableAmount().toString(),
                "taxAmount", o -> o.getTaxAmount().toString(),
                "paymentStatus", o -> o.getPaymentStatus(),
                "orderQty", o -> o.getOrderItemEntityList().size(),
                "orderStatus", o -> o.getOrderStatus().toString()
        );
    }
}
