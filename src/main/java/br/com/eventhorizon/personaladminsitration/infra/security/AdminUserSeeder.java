package br.com.eventhorizon.personaladminsitration.infra.security;

import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import br.com.eventhorizon.personaladminsitration.register.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AdminUserSeeder(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe algum usuário no banco
        if (userRepository.count() == 0) {
            UserEntity admin = new UserEntity();
            admin.setName("Administrador");
            admin.setEmail("admin@eventhorizon.com");
            // Criptografa a senha inicial "admin123"
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setActive(true);

            userRepository.save(admin);
        }
    }
}