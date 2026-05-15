package br.com.eventhorizon.personaladminsitration.commom.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
