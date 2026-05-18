package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto;

import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;

import java.math.BigDecimal;

public record ReceivableMovementUpdateDto (
        BigDecimal movementValue,
        BigDecimal interestValue,
        BigDecimal discountValue,
        MovementType movementType,
        String movementObservation
){}
