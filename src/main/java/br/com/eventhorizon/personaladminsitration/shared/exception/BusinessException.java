package br.com.eventhorizon.personaladminsitration.shared.exception;

public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }
}
