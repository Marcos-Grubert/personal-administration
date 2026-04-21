package br.com.eventhorizon.personaladminsitration.register.users;

import br.com.eventhorizon.personaladminsitration.register.users.dto.UserCreateDto;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import br.com.eventhorizon.personaladminsitration.register.users.exeption.EmailAlreadyInUseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UserResponseDto create(UserCreateDto userCreateDto) {
        if(userRepository.findByEmail(userCreateDto.email()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail informado já está em uso.");
        }
        UserEntity user = UserMapper.toEntity(userCreateDto);
        user.setPasswordHash(new BCryptPasswordEncoder().encode(userCreateDto.password()));
        user.setActive(true);
        user.setCreatedBy(1L);
        UserEntity savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}
