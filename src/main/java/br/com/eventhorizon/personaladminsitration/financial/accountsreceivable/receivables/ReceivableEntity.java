package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.register.customers.CustomerEntity;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "receivables")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter @Setter
public class ReceivableEntity {
    @EmbeddedId
    private ReceivableId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli", referencedColumnName = "codcli", insertable = false, updatable = false)
    private CustomerEntity customer;

    @Column(name = "destit", length = 500)
    private String destit;

    @Column(name = "vlrori", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalValue;

    @Column(name = "vlrtit", nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingValue;

    @Column(name = "sittit", nullable = false, columnDefinition = "SMALLINT")
    private FinancialStatus financialStatus;

    @Column(name = "sitant",columnDefinition = "SMALLINT")
    private FinancialStatus financialPreviousStatus;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuger", nullable = false, updatable = false, referencedColumnName = "codusu")
    private UserEntity createdBy;

    @Column(name = "datger", updatable = false)
    Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuatu",referencedColumnName = "codusu")
    private UserEntity updatedBy;

    @Column(name = "datatu")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        if(this.remainingValue == null) {
            this.remainingValue = this.originalValue;
        }

        if(this.financialPreviousStatus == null) {
            this.financialPreviousStatus = this.financialStatus;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
