package br.com.eventhorizon.personaladminsitration.register.users;

import br.com.eventhorizon.personaladminsitration.register.users.dto.UserCreateDto;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(UserCreateDto userCreateDto){
        UserEntity user = new UserEntity();
        user.setName(userCreateDto.name());
        user.setEmail(userCreateDto.email());
        return user;
    }

    public UserResponseDto toResponse(UserEntity user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.isActive()
        );
    }
}
