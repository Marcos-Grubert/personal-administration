package br.com.eventhorizon.personaladminsitration.register.users.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String message,
        LocalDateTime timestamp
) {
}
