package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto;

import br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.ReceivableId;
import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import br.com.eventhorizon.personaladminsitration.register.users.dto.UserResponseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record ReceivablePendingCollectionDto(
        ReceivableId id,
        String customerName,
        BigDecimal originalValue,
        BigDecimal remainingValue,
        String destit,
        FinancialStatus financialStatus,
        LocalDate originalDueDate,
        UserResponseDto createdBy,
        Instant createdAt
) {
}
