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
import java.time.LocalDateTime;
import java.util.Map;

import static org.adrian.mypetclinic.exception.ExceptionAttributes.*;

@RestControllerAdvice
@Slf4j
class PetclinicExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> constraintViolationException(ConstraintViolationException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ExceptionHandlingResponseBuilder builder = this.builder(status);
        e.getConstraintViolations().forEach(
                constraintViolation -> this.putConstraintViolation(builder, constraintViolation));
        return ResponseEntity.status(status).body(builder.build());
    }

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> petClinicBusinessException(PetClinicBusinessException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                this.builder(status)
                        .put(MESSAGE, String.valueOf(e.getMessage()))
                        .put(PROPERTY_PATH, String.valueOf(e.getPropertyPath()))
                        .put(INVALID_VALUE, String.valueOf(e.getInvalidValue()))
                        .put(MESSAGE_TEMPLATE, e.getMessageTemplate())
                        .build());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ExceptionHandlingResponseBuilder builder = this.builder(badRequest);
        ex.getBindingResult().getFieldErrors().stream()
                .filter(fieldError -> fieldError.contains(ConstraintViolation.class))
                .map(fieldError -> fieldError.unwrap(ConstraintViolation.class))
                .forEach(constraintViolation -> this.putConstraintViolation(builder, constraintViolation));

        if (builder.hasErrors()) {
            return ResponseEntity.status(badRequest).body(builder.build());
        } else {
            return super.handleMethodArgumentNotValid(ex, headers, status, request);
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
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        super.handleExceptionInternal(ex, body, headers, status, request);
        String message = ex.getMessage();
        log.error(message, ex);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(this.builder(status)
                        .put(MESSAGE, message)
                        .build());
    }

    private ExceptionHandlingResponseBuilder builder(HttpStatus status) {
        return new ExceptionHandlingResponseBuilder()
                .globalInfo(TIMESTAMP, LocalDateTime.now())
                .globalInfo(STATUS, status.value());
    }
}
