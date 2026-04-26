package br.com.eventhorizon.personaladminsitration.infra.security;

import br.com.eventhorizon.personaladminsitration.register.users.RefreshToken;
import br.com.eventhorizon.personaladminsitration.register.users.RefreshTokenRepository;
import br.com.eventhorizon.personaladminsitration.register.users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    //Cria o token no banco
    @Transactional
    public RefreshToken createRefreshToken(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        RefreshToken refreshToken = refreshTokenRepository.findByUser(user).orElse(new RefreshToken());
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        // Define validade de 7 dias (604800 segundos)
        refreshToken.setExpiryDate(Instant.now().plusSeconds(604800));

        return refreshTokenRepository.save(refreshToken);
    }

    //Busca o token no banco
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    //Verifica se o token expirou
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expirou. Por favor, realize um novo login.");
        }
        return token;
    }

    //Deleta o token (util para logout)
    public void deleteByUserId(String email) {
        userRepository.findByEmail(email).ifPresent(refreshTokenRepository::deleteByUser);
    }
}
