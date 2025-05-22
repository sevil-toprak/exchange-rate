package com.api.exchangerate.repository;

import com.api.exchangerate.entity.ExchangeHistory;
import com.api.exchangerate.model.request.ExchangeRateHistoryRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ExchangeHistoryCustomRepositoryImpl implements ExchangeHistoryCustomRepository {

    @PersistenceContext
    EntityManager entityManager;

    private static final String SELECT_QUERY = "SELECT eh FROM ExchangeHistory eh WHERE 1=1 ";
    private static final String COUNT_QUERY = "SELECT COUNT(eh.id) FROM ExchangeHistory eh WHERE 1=1 ";

    public Page<ExchangeHistory> filterExchangeRateHistory(ExchangeRateHistoryRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getLimit());

        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        buildQueryTransactionId(request, queryBuilder, parameters);
        buildQueryCreatedDate(request, queryBuilder, parameters);

        // Get total count after generating query
        Long totalCount = getTotalCount(queryBuilder, parameters);

        // Filter query
        TypedQuery<ExchangeHistory> query = getFilterTypedQuery(queryBuilder, parameters);

        // Set pagination
        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());

        List<ExchangeHistory> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageRequest, totalCount);
    }

    private TypedQuery<ExchangeHistory> getFilterTypedQuery(StringBuilder queryBuilder, Map<String, Object> parameters) {
        String queryString = SELECT_QUERY + queryBuilder.toString();
        TypedQuery<ExchangeHistory> query = entityManager.createQuery(queryString, ExchangeHistory.class);
        setQueryParameters(parameters, query);
        return query;
    }

    private Long getTotalCount(StringBuilder queryBuilder, Map<String, Object> parameters) {
        String queryString = COUNT_QUERY + queryBuilder.toString();
        TypedQuery<Long> query = entityManager.createQuery(queryString, Long.class);
        setCountQueryParameters(parameters, query);
        return query.getSingleResult();
    }

    private void buildQueryCreatedDate(ExchangeRateHistoryRequest request, StringBuilder queryBuilder, Map<String, Object> parameters) {
        if (request.getStartDate() == null || request.getEndDate() == null) {
            return;
        }

        LocalDateTime startDate = request.getStartDate();
        queryBuilder.append(" AND eh.createdAt >= :startDate ");
        parameters.put("startDate", startDate);

        LocalDateTime endDate = request.getEndDate();
        queryBuilder.append(" AND eh.createdAt <= :endDate ");
        parameters.put("endDate", endDate);
    }

    private void buildQueryTransactionId(ExchangeRateHistoryRequest request, StringBuilder queryBuilder, Map<String, Object> parameters) {
        final String transactionId = request.getTransactionId();
        if (transactionId != null) {
            queryBuilder.append(" AND eh.transactionId = :transactionId ");
            parameters.put("transactionId", transactionId);
        }
    }

    private void setCountQueryParameters(Map<String, Object> parameters, TypedQuery<Long> countQuery) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
    }

    private void setQueryParameters(Map<String, Object> parameters, TypedQuery<ExchangeHistory> query) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

}

