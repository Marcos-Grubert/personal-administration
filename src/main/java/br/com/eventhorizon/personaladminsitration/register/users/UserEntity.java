package br.com.eventhorizon.personaladminsitration.register.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codusu")
    private Long userId;

    @Column(name = "nomusu", nullable = false, length = 120)
    private String name;

    @Column(name= "emausu", nullable = false, unique = true, length = 180)
    private String email;

    @Column(name= "usupas", nullable = false,  length = 255)
    private String passwordHash;

    @Column(name= "situsu", nullable = false)
    private boolean active;

    @Column(name= "usuger", updatable = false)
    private Long createdBy;

    @Column(name= "datger", updatable = false)
    private Instant createdAt;

    @Column(name = "usuatu")
    private Long updatedBy;

    @Column(name = "datatu")
    private Instant updatedAt;

    //Métodos obrigatórios para implementar UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // No mercado, se você não tem tabelas de perfis ainda, retornamos um nível padrão
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return this.email; // Define que o login será feito via e-mail
    }

    @Override
    public String getPassword() {
        return this.passwordHash; // Mapeia para o campo de senha criptografada
    }

    @Override
    public boolean isEnabled() {
        return this.active; // Se 'situsu' for false, o Spring bloqueia o login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
