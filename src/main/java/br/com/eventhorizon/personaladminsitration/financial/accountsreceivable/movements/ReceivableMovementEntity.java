package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableEntity;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
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
@Table(name = "receivable_movements")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
@Setter
@DynamicUpdate
public class ReceivableMovementEntity {
    @Version
    private Long version;

    @EmbeddedId
    private ReceivableMovementId id;

    @MapsId("receivable")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "codcli", referencedColumnName = "codcli"),
            @JoinColumn(name = "numtit", referencedColumnName = "numtit")
    })
    private ReceivableEntity receivable;

    @Column(name = "vlrmov",nullable = false, precision = 15, scale = 2)
    private BigDecimal movementValue;

    @Column(name = "vlrjrs", nullable = false, precision = 15, scale = 2)
    private BigDecimal interestValue = BigDecimal.ZERO;

    @Column(name = "vlrdsc", nullable = false, precision = 15, scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipmov", nullable = false)
    private MovementType movementType;

    @Column(name = "vctant")
    private LocalDate previousDueDate;

    @Column(name = "vctatu")
    private LocalDate nextDueDate;

    @Column(name = "datmov", nullable = false)
    private Instant movementInstant;

    @Column(name = "obsmov", length = 500)
    private String movementObservation;

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuger", referencedColumnName = "codusu", nullable = false, updatable = false)
    private UserEntity createdBy;

    @CreatedDate
    @Column(name = "datger", nullable = false, updatable = false)
    private Instant createdAt;
}
