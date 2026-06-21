package br.com.eventhorizon.personaladminsitration.register.customers.dto;

import br.com.eventhorizon.personaladminsitration.register.customers.CustomerEntity;
import br.com.eventhorizon.personaladminsitration.register.enums.PersonType;

public record CustomerLookupDto(
        Long id,
        String name,
        PersonType type,
        String email,
        String documentCode
) {
    public CustomerLookupDto(CustomerEntity customerEntity) {
        this(
                customerEntity.getId(),
                customerEntity.getName(),
                customerEntity.getType(),
                customerEntity.getEmail(),
                customerEntity.getDocumentCode()
        );
    }
}
