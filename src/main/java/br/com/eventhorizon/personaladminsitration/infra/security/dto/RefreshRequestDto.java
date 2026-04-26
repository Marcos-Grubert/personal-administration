package br.com.eventhorizon.personaladminsitration.infra.security.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(
        @NotBlank
        String refreshToken
) {
}
