package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto;

import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableId;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;

import java.math.BigDecimal;
import java.time.Instant;

public record ReceivableResponseDto(
        ReceivableId id,
        BigDecimal originalValue,
        String destit,
        FinancialStatus financialStatus,
        UserResponseDto createdBy,
        Instant createdAt
) {
}
