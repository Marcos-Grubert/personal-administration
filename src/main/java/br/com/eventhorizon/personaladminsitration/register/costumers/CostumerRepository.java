package br.com.eventhorizon.personaladminsitration.register.costumers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CostumerRepository extends JpaRepository<CostumerEntity, Long> {
    Optional<CostumerEntity> findByEmail(String email);
}
