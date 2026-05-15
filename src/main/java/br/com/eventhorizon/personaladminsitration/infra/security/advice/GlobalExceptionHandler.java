package br.com.eventhorizon.personaladminsitration.infra.security.advice;

import br.com.eventhorizon.personaladminsitration.commom.exception.BusinessException;
import br.com.eventhorizon.personaladminsitration.register.custumers.exception.ResourceNotFoundException;
import br.com.eventhorizon.personaladminsitration.register.users.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyInUseException(BusinessException ex){
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handle401() {
        // Usando o mesmo DTO para manter o padrão visual do JSON
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.UNAUTHORIZED.value(),
                "E-mail ou senha incorretos.",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDto error = new ErrorResponseDto(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}