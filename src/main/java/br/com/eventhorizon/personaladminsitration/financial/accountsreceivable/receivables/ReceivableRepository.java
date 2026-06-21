package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReceivableRepository extends JpaRepository<ReceivableEntity, ReceivableId> {

    @Query("SELECT r FROM ReceivableEntity r " +
            "JOIN FETCH r.customer c " + // Alias 'c' definido aqui
            "LEFT JOIN FETCH r.createdBy " +
            "WHERE r.financialStatus IN (:statuses) " +
            "AND r.originalDueDate BETWEEN :startDate AND :endDate " +
            "AND (:customerId IS NULL OR c.id = :customerId) " +
            "ORDER BY r.originalDueDate ASC")
    Page<ReceivableEntity> findPendingReceivablesByDueDateInterval(
            @Param("statuses") List<FinancialStatus> statuses,
            @Param("customerId") Long customerId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
