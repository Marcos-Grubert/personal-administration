package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ReceivableMovementRepository extends JpaRepository<ReceivableMovementEntity, ReceivableMovementId> {
    @Query("SELECT COALESCE(MAX(mov.id.sequenceMovement),0) " +
            "FROM ReceivableMovementEntity mov " +
            "WHERE mov.id.receivableId.customerId = :customerId " +
            "AND mov.id.receivableId.document = :document")
    Integer findMaxSequence(@Param("customerId") Long customerId, @Param("document") String document);

    @Query("SELECT SUM(mov.movementValue) " +
            "FROM ReceivableMovementEntity mov " +
            "WHERE mov.id.receivableId.customerId = :customerId " +
            "AND mov.id.receivableId.document = :document")
    BigDecimal getTotalMovementValue(@Param("customerId") Long customerId, @Param("document") String document);
}
