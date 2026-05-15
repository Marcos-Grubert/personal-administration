package br.com.eventhorizon.personaladminsitration.register.custumers.exception;

import br.com.eventhorizon.personaladminsitration.commom.exception.BusinessException;

public class EmailAlreadyInUseException extends BusinessException {
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}