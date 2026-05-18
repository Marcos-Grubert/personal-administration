package br.com.eventhorizon.personaladminsitration.financial.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MovementType {
    INCLUSAO(0),
    BAIXA(1),
    ESTORNO(2),
    CANCELAMENTO(3);
    private final int id;

    MovementType(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    public static MovementType fromId(int id) {
        for (MovementType type : MovementType.values()) {
            if (type.getId() == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo inválido: " + id);
    }
}
