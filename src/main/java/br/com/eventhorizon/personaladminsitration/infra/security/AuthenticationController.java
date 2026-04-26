package br.com.eventhorizon.personaladminsitration.infra.security;

import br.com.eventhorizon.personaladminsitration.infra.security.dto.AuthenticationDto;
import br.com.eventhorizon.personaladminsitration.infra.security.dto.LoginResponseDto;
import br.com.eventhorizon.personaladminsitration.infra.security.dto.RefreshRequestDto;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var user = (UserEntity) auth.getPrincipal();

        //Gera o Access Token (JWT) e sua expiração
        var token = tokenService.generateToken(user);
        var expirationDate = tokenService.getExpirationDate();

        //Gera o refreshtoken (salva no banco)
        var refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        //Converte a data do refresh (Instant para localdatetime)
        var refreshExpirationInLocalDateTime = java.time.LocalDateTime.ofInstant(
                refreshToken.getExpiryDate(),
                java.time.ZoneId.systemDefault()
        );

        // Retornando o Token, o tipo "Bearer" e a duração de 7200 segundos (2 horas)
        return ResponseEntity.ok(new LoginResponseDto(
                refreshToken.getToken(),
                refreshExpirationInLocalDateTime,
                "Bearer",
                token,
                expirationDate));
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        // Pegamos o usuário logado do contexto do Spring Security
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        var user = (UserEntity) auth.getPrincipal();

        // Deletamos o Refresh Token do banco para que ele não possa mais ser usado
        refreshTokenService.deleteByUserId(user.getEmail());

        return ResponseEntity.ok().build(); // Retorna 200 OK
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody @Valid RefreshRequestDto data) {
        return refreshTokenService.findByToken(data.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    var user = refreshToken.getUser();
                    var newToken = tokenService.generateToken(user);
                    var expirationDate = tokenService.getExpirationDate();

                    var refreshExpirationInLocalDateTime = java.time.LocalDateTime.ofInstant(
                            refreshToken.getExpiryDate(),
                            java.time.ZoneId.systemDefault()
                    );

                    return ResponseEntity.ok(new LoginResponseDto(
                            refreshToken.getToken(),
                            refreshExpirationInLocalDateTime,
                            "Bearer",
                            newToken,
                            expirationDate));
                })
                .orElseThrow(() -> new RuntimeException("Refresh token inválido ou não encontrado!"));
    }
}
