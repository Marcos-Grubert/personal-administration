package br.com.eventhorizon.personaladminsitration.financial.accountsreceivable.receivables.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceivableUpdateDto(
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

        @NotNull(message = "Obrigatório o preenchimento da data de vencimento.")
        @FutureOrPresent(message = "A data de vencimento não pode ser anterior à data de hoje")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate originalDueDate
) {
}
