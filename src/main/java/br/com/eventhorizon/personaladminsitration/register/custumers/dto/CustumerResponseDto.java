package br.com.eventhorizon.personaladminsitration.register.custumers.dto;

import br.com.eventhorizon.personaladminsitration.enums.PersonType;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;

import java.time.Instant;

public record CustumerResponseDto(
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
