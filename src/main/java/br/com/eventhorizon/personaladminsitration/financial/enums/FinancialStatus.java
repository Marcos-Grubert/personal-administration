package br.com.eventhorizon.personaladminsitration.financial.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FinancialStatus {
    ABERTO(1),
    LIQUIDADO(4),
    CANCELADO(5);

    private final int id;

    FinancialStatus(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    public static FinancialStatus fromId(int id) {
        for (FinancialStatus status : FinancialStatus.values()) {
            if (status.getId() == id) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + id);
    }
}
