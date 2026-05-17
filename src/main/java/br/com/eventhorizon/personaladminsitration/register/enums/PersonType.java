package br.com.eventhorizon.personaladminsitration.register.enums;

public enum PersonType {
    INDIVIDUAL ("F", "Física"),
    COMPANY ("J", "Jurídica");

    private final String letter;
    private final String description;

    PersonType(String letter, String description) {
        this.letter = letter;
        this.description = description;
    }

    public String getLetter() {
        return letter;
    }

    public String getDescription() {
        return description;
    }
}
