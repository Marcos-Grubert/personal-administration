package br.com.eventhorizon.personaladminsitration.register.custumers.dto;

import br.com.eventhorizon.personaladminsitration.enums.PersonType;

public record CustumerCreateDto(
        String name,
        PersonType type,
        String documentCode,
        String email,
        Boolean situation
) {
}
