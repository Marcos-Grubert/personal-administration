package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceivableRepository extends JpaRepository<ReceivableEntity, ReceivableId> {
}
