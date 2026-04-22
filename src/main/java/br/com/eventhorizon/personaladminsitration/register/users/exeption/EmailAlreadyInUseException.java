package br.com.eventhorizon.personaladminsitration.register.users.exeption;

public class EmailAlreadyInUseException extends RuntimeException{
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
