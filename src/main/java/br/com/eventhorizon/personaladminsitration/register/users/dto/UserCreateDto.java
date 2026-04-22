package br.com.eventhorizon.personaladminsitration.register.users.dto;

public record UserCreateDto(
        //Atributos passados para classe do tipo record implicitamente são do tipo private final
        String name,
        String email,
        String password
) {
}
