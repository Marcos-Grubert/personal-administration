package br.com.eventhorizon.personaladminsitration.register.customers.dto;

import br.com.eventhorizon.personaladminsitration.register.enums.PersonType;

public record CustomerUpdateDto(
        String name,
        PersonType type,
        String documentCode,
        String email,
        Boolean situation
) {
}
