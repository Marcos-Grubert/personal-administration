package br.com.eventhorizon.personaladminsitration.register.users;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUser(UserEntity user);

    @Modifying
    @Transactional
    int deleteByUser(UserEntity user); // Útil para deslogar o usuário de todos os lugares
}