package br.com.eventhorizon.personaladminsitration.register.users;

import br.com.eventhorizon.personaladminsitration.commom.exception.EmailAlreadyInUseException;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserCreateDto;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UserResponseDto create(UserCreateDto userCreateDto) {
        if(userRepository.findByEmail(userCreateDto.email()).isPresent()) {
            throw new EmailAlreadyInUseException("E-mail informado já está em uso.");
        }
        UserEntity user = userMapper.toEntity(userCreateDto);
        user.setPasswordHash(passwordEncoder.encode(userCreateDto.password()));
        user.setActive(true);
        user.setCreatedBy(1L);
        UserEntity savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public List<UserResponseDto> findAll(){
        List<UserResponseDto> userResponseDto = new ArrayList<>();
        userRepository.findAll().forEach(user -> userResponseDto.add(userMapper.toResponse(user)));
        return userResponseDto;
    }
}
