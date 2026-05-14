package br.com.eventhorizon.personaladminsitration.infra.security;

import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<UserEntity> {
    @Override
    public Optional<UserEntity> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        // Aqui você retorna a sua entidade de usuário que está logada
        return Optional.of((UserEntity) authentication.getPrincipal());
    }
}
