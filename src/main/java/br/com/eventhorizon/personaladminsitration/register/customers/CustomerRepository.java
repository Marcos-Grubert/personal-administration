package br.com.eventhorizon.personaladminsitration.register.customers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    boolean existsByEmail(String email);

    Page<CustomerEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<CustomerEntity> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<CustomerEntity> findByDocumentCodeContainingIgnoreCase(String documentCode, Pageable pageable);

}
