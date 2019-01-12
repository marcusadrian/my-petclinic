package org.adrian.mypetclinic.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
class PetclinicExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler
    ResponseEntity<?> noSuchElementException(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> constraintViolationException(ConstraintViolationException e) {
        ExceptionHandlingResponseBuilder builder = this.builder();
        e.getConstraintViolations().forEach(constraintViolation ->
                builder.newError()
                        .put("message", String.valueOf(constraintViolation.getMessage()))
                        .put("propertyPath", String.valueOf(constraintViolation.getPropertyPath()))
                        .put("invalidValue", String.valueOf(constraintViolation.getInvalidValue()))
                        .put("messageTemplate", constraintViolation.getMessageTemplate())
        );
        return ResponseEntity.badRequest().body(builder.getErrorMap());
    }

    @ExceptionHandler
    ResponseEntity<Map<String, Object>> petClinicBusinessException(PetClinicBusinessException e) {
        return ResponseEntity.badRequest().body(
                this.builder()
                        .put("message", String.valueOf(e.getMessage()))
                        .put("propertyPath", String.valueOf(e.getPropertyPath()))
                        .put("invalidValue", String.valueOf(e.getInvalidValue()))
                        .put("messageTemplate", e.getMessageTemplate()).getErrorMap());
    }

    private ExceptionHandlingResponseBuilder builder() {
        return new ExceptionHandlingResponseBuilder()
                .globalInfo("timestamp", LocalDateTime.now());
    }
}
