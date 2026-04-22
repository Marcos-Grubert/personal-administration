package br.com.eventhorizon.personaladminsitration.register.users.dto;

public record UserResponseDto(
        Long userId,
        String name,
        String email,
        Boolean active
) {
}
