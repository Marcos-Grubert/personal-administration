package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.movements.dto;

import br.com.eventhorizon.personaladminsitration.financial.enums.MovementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceivableMovementRollbackDto(
        @NotNull(message = "O código do cliente é obrigatório")
        Long customerId,
        @NotBlank(message = "A informação do título é obrigatório")
        String document,
        @NotNull(message = "O tipo de movimento é obrigatório (2 - Estorno, 3 - Cancelamento)")
        MovementType movementType,
        String movementObservation
) {
}
