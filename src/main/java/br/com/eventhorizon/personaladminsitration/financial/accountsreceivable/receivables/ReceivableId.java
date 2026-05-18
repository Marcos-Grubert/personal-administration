package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReceivableId implements Serializable {
    @Column(name = "codcli", nullable = false)
    private Long customerId;

    @Column(name = "numtit", nullable = false, length = 120)
    private String document;
}
