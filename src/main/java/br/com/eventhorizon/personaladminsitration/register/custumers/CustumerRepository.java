package br.com.eventhorizon.personaladminsitration.register.custumers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustumerRepository extends JpaRepository<CustumerEntity, Long> {
    boolean existsByEmail(String email);
}
