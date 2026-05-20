package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.register.customers.CustomerEntity;
import br.com.eventhorizon.personaladminsitration.register.users.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "receivables")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter @Setter
@DynamicUpdate
public class ReceivableEntity {

    @Version
    private Long version;

    @EmbeddedId
    private ReceivableId id;
    @MapsId("customer")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codcli", referencedColumnName = "codcli")
    private CustomerEntity customer;

    @Column(name = "destit", length = 500)
    private String destit;

    @Column(name = "vlrori", nullable = false, precision = 15, scale = 2)
    private BigDecimal originalValue;

    @Column(name = "vlrabe", nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingValue;

    @Column(name = "sittit", nullable = false, columnDefinition = "SMALLINT")
    private FinancialStatus financialStatus;

    @Column(name = "vctori", nullable = false, updatable = false)
    private LocalDate originalDueDate;

    @Column(name = "vcttit", nullable = false)
    private LocalDate dueDate;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuger", nullable = false, updatable = false, referencedColumnName = "codusu")
    private UserEntity createdBy;

    @CreatedDate
    @Column(name = "datger", updatable = false)
    Instant createdAt;

    @PrePersist
    public void prePersist() {
        if(this.remainingValue == null) {
            this.remainingValue = this.originalValue;
        }

        if(this.financialStatus == null) {
            this.financialStatus = FinancialStatus.ABERTO;
        }

        if(this.dueDate == null) {
            this.dueDate = this.originalDueDate;
        }
    }
}
