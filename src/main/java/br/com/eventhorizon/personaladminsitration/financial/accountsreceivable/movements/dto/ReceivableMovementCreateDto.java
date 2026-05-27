package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto;
import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ReceivableMovementCreateDto (
        @NotNull(message = "O código do cliente é obrigatório")
        Long customerId,
        @NotBlank(message = "A informação do título é obrigatorio")
        String document,
        @PositiveOrZero(message = "O valor do movimento precisa ser positivo ou zero")
        BigDecimal movementValue,
        @PositiveOrZero(message = "O valor de juros deve ser positivo ou zero")
        BigDecimal interestValue,
        @PositiveOrZero(message = "O valor do desconto deve ser positivo ou zero")
        BigDecimal discountValue,
        @NotNull(message = "O tipo de movimento é obrigatório 1 - Baixa, 2 - Estorno, 3 - Cancelamento")
        MovementType movementType,
        String movementObservation
){}
