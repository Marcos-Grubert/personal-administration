package br.com.eventhorizon.personaladminsitration.register.customers;

import br.com.eventhorizon.personaladminsitration.register.enums.PersonType;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "customers")
@Getter
@Setter
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codcli")
    private Long id;

    @Column(name = "nomcli", nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipcli", nullable = false, length = 20)
    private PersonType type;

    @Column(name = "cgccpf", nullable = false, length = 20)
    private String documentCode;

    @Column(name = "emacli",unique = true,nullable = false,length = 180)
    private String email;

    @Column(name = "sitcli",nullable = false)
    private boolean situation = true;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuger", referencedColumnName = "codusu")
    private UserEntity userRegister;

    @CreatedDate
    @Column(name = "datger", updatable = false)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuatu", referencedColumnName = "codusu")
    private UserEntity userUpdate;

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
