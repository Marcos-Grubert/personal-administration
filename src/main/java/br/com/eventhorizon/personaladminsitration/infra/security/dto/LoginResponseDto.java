package br.com.eventhorizon.personaladminsitration.infra.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record LoginResponseDto(
        String refreshToken,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime refreshTokenExpiresAt,
        String type,
        String token,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expiresAt
) {
}
