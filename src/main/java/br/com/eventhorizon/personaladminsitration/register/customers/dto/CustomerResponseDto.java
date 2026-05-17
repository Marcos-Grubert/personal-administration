package br.com.eventhorizon.personaladminsitration.register.customers.dto;

import br.com.eventhorizon.personaladminsitration.register.enums.PersonType;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;

import java.time.Instant;

public record CustomerResponseDto(
        Long id,
        String name,
        PersonType type,
        String documentCode,
        String email,
        Boolean situation,
        UserResponseDto userRegister,
        Instant createdAt
) {
}
