package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto;
import br.com.eventhorizon.personaladminsitration.financial.enums.FinancialStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record ReceivableCreateDto(
        @NotNull(message = "O código do cliente é obrigatório")
        @Positive(message = "O código do cliente deve ser um número positivo")
        Long customerCode,

        @NotBlank(message = "É necessário informar o documento (nome) do titulo")
        @Length(min = 1, max = 120, message = "O documento deve ter no mínimo 1 caráctere e no máximo 120")
        String document,

        @Length(max = 500, message = "O máximo de carácteres permitido é 500")
        String destit,

        @NotNull(message = "O valor original do título é obrigatório")
        BigDecimal originalValue,

        @NotNull(message = "A situação do título é obrigatória")
        FinancialStatus financialStatus
) {
}
