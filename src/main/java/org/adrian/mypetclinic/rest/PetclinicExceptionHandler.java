package org.adrian.mypetclinic.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
class PetclinicExceptionHandler {

    @ExceptionHandler
    ResponseEntity<?> noSuchElementException(NoSuchElementException e) {
        return ResponseEntity.notFound().build();
    }
}
