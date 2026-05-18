package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.ReceivableMovementId;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

public record ReceivableMovementResponseDto (
        ReceivableMovementId id,
        BigDecimal movementValue,
        BigDecimal interestValue,
        BigDecimal discountValue,
        MovementType movementType,
        String movementObservation,
        UserResponseDto createdBy,
        Instant createdAt
){
}
