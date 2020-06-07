package org.adrian.mypetclinic.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;

import static org.adrian.mypetclinic.exception.ExceptionAttributes.*;

@RestControllerAdvice
@Slf4j
class PetclinicExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> constraintViolationException(ConstraintViolationException e) {
        ExceptionHandlingResponseBuilder builder = this.builder(e, HttpStatus.BAD_REQUEST);
        e.getConstraintViolations().forEach(
                constraintViolation -> this.putConstraintViolation(builder, constraintViolation));
        return ResponseEntity.status(builder.getStatus()).body(builder.build());
    }

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> petClinicBusinessException(PetClinicBusinessException e) {
        ExceptionHandlingResponseBuilder builder = this.builder(e, HttpStatus.BAD_REQUEST)
                .put(MESSAGE, String.valueOf(e.getMessage()))
                .put(PROPERTY_PATH, String.valueOf(e.getPropertyPath()))
                .put(INVALID_VALUE, String.valueOf(e.getInvalidValue()))
                .put(MESSAGE_TEMPLATE, e.getMessageTemplate());
        return ResponseEntity.status(builder.getStatus()).body(builder.build());
    }

    @ExceptionHandler
    ResponseEntity<Object> fallback(Exception e, HttpHeaders headers, WebRequest request) {
        ExceptionHandlingResponseBuilder builder = this.builder(e, HttpStatus.BAD_REQUEST)
                .put(MESSAGE, String.valueOf(e.getMessage()));
        return this.handleExceptionInternal(e, builder.build(), headers, builder.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionHandlingResponseBuilder builder = this.builder(e, HttpStatus.BAD_REQUEST);
        e.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> fieldError.contains(ConstraintViolation.class))
                .map(fieldError -> fieldError.unwrap(ConstraintViolation.class))
                .forEach(constraintViolation -> this.putConstraintViolation(builder, constraintViolation));

        if (builder.hasErrors()) {
            return ResponseEntity.status(builder.getStatus()).body(builder.build());
        } else {
            return super.handleMethodArgumentNotValid(e, headers, status, request);
        }
    }

    private ExceptionHandlingResponseBuilder putConstraintViolation(ExceptionHandlingResponseBuilder builder,
                                                                    ConstraintViolation<?> constraintViolation) {
        return builder.newError()
                .put(MESSAGE, String.valueOf(constraintViolation.getMessage()))
                .put(PROPERTY_PATH, String.valueOf(constraintViolation.getPropertyPath()))
                .put(INVALID_VALUE, String.valueOf(constraintViolation.getInvalidValue()))
                .put(MESSAGE_TEMPLATE, constraintViolation.getMessageTemplate());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleExceptionInternal(e, body, headers, status, request);
        String message = e.getMessage();
        log.error(message, e);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(this.builder(e, status)
                        .put(MESSAGE, message)
                        .build());
    }

    private ExceptionHandlingResponseBuilder builder(Exception e, HttpStatus status) {
        return new ExceptionHandlingResponseBuilder(e, status);
    }
}
