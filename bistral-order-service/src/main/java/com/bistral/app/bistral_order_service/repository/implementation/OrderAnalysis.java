package com.bistral.app.bistral_order_service.repository.implementation;

import com.bistral.app.bistral_order_service.dtos.OrderResponse;
import com.bistral.app.bistral_order_service.dtos.PageResponse;
import com.bistral.app.bistral_order_service.dtos.TrendPointDtoImpl;
import com.bistral.app.bistral_order_service.entity.OrderEntity;
import com.bistral.app.bistral_order_service.repository.interfaces.IOrderAnalysis;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class OrderAnalysis implements IOrderAnalysis {

    private final EntityManager entityManager;


    @Override
    public List<TrendPointDtoImpl> getOrderTrend(String sql, LocalDateTime start, LocalDateTime end) {
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // label
                        ((Number) row[1]).doubleValue()      // value
                ))
                .toList();
    }

    @Override
    public List<TrendPointDtoImpl> getRevenueTrend(String sql, LocalDateTime start, LocalDateTime end) {
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // label
                        ((Number) row[1]).doubleValue()      // value
                ))
                .toList();
    }

    @Override
    public List<TrendPointDtoImpl> getPaymentModeTrend(String sql, LocalDateTime start, LocalDateTime end) {

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("start", start);
        query.setParameter("end", end);

        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new TrendPointDtoImpl(
                        (String) row[0],                     // paymentMode
                        ((Number) row[1]).doubleValue()      // totalAmount
                ))
                .toList();
    }

    public PageResponse<List<OrderEntity>> orderResponsesFilterd(
            List<UUID> bistroIds, List<UUID> branchId,
            BigDecimal minPayableAmount,
            BigDecimal maxPayableAmount,
            LocalDate from,
            LocalDate to,
            int page,
            int size
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> cq = cb.createQuery(OrderEntity.class);
        Root<OrderEntity> root = cq.from(OrderEntity.class);
        List<Predicate> predicates = buildPredicates(cb
                , root, bistroIds, branchId, minPayableAmount, maxPayableAmount, from, to);
        cq.where(cb.and(predicates.toArray(Predicate[]::new)));
        TypedQuery<OrderEntity> query = entityManager.createQuery(cq);
        // to implmenet Pagingation
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<OrderEntity> res = query.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<OrderEntity> countRoot = countQuery.from(OrderEntity.class);
        countQuery.select(cb.count(countRoot));
        predicates = buildPredicates(cb, countRoot, bistroIds, branchId, minPayableAmount, maxPayableAmount, from
                , to);
        countQuery.where(cb.and(predicates.toArray(new Predicate[0])));
        long total = entityManager.createQuery(countQuery).getSingleResult();
        long totalPage = (long) (Math.ceil((double) total / size));
        return PageResponse.<List<OrderEntity>>builder()
                .data(res)
                .totalPage(totalPage)
                .crnPage(page + 1)
                .size(size)
                .totalRecords(total)
                .hasNext(page + 1 < totalPage)
                .hasPrevious(page - 1 > 0)
                .build();
    }

    private List<Predicate> buildPredicates(
            CriteriaBuilder cb,
            Root<OrderEntity> root,
            List<UUID> bistroIds,
            List<UUID> branchIds,
            BigDecimal minPayableAmount,
            BigDecimal maxPayableAmount,
            LocalDate from,
            LocalDate to
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (bistroIds != null && !bistroIds.isEmpty()) {
            predicates.add(root.get("bistroId").in(bistroIds));
        }

        if (branchIds != null && !branchIds.isEmpty()) {
            predicates.add(root.get("branchId").in(branchIds));
        }
        if (minPayableAmount != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("payableAmount"), minPayableAmount));
        }

        if (maxPayableAmount != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("payableAmount"), maxPayableAmount));
        }
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), from.atStartOfDay()));
        }

        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), to.atTime(23, 59, 59)));
        }

        return predicates;
    }


}
