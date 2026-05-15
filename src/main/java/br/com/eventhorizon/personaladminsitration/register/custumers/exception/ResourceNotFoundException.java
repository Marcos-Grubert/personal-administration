package br.com.eventhorizon.personaladminsitration.register.custumers.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
