package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ReceivableMovementId implements Serializable {
    //Sobrescreve o nome das colunas para não utilizar o nome da classe receivableId + o nome da coluna
    @AttributeOverrides({
            @AttributeOverride(name = "customerId", column = @Column(name = "codcli")),
            @AttributeOverride(name = "document", column = @Column(name = "numtit"))
    })
    private ReceivableId receivableId;
    @Column(name = "seqmov", nullable = false)
    private Integer sequenceMovement;
}
