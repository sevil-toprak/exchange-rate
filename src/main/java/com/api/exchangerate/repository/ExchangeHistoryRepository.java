package com.api.exchangerate.repository;

import com.api.exchangerate.entity.ExchangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeHistoryRepository extends JpaRepository<ExchangeHistory, Long>, ExchangeHistoryCustomRepository {

    // this class can be used for uncomplicated queries.

    /*
     * For filtering and querying the total count, a JPQL query like the one below can be used (or a native query if needed).
     * Although it's a longer approach, I prefer using a custom repository in cases
     * where there are many null checks in the filtering logic.
     * */
    /****
     @Query(""" SELECT COUNT(eh.id)
     FROM   ExchangeHistory eh
     WHERE  (:transactionId IS NULL OR eh.transactionId = :transactionId)
     AND    (:startDate     IS NULL OR eh.createdAt     >= :startDate)
     AND    (:endDate       IS NULL OR eh.createdAt     <= :endDate)
     """)
     long filterExchangeHistory(String transactionId, LocalDateTime startDate, LocalDateTime endDate);
     *****/
}

