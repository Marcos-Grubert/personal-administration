package br.com.eventhorizon.personaladminsitration.register.costumers.dto;

import br.com.eventhorizon.personaladminsitration.enums.PersonType;

public record CostumerCreateDto(
        String name,
        PersonType type,
        String documentCode,
        String email,
        Boolean situation
) {
}
