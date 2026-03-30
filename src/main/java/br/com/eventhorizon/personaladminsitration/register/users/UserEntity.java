package br.com.eventhorizon.personaladminsitration.register.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
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

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
