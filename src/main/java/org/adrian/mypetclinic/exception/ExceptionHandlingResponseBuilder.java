package org.adrian.mypetclinic.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.adrian.mypetclinic.exception.ExceptionAttributes.*;


public class ExceptionHandlingResponseBuilder {
    private final List<Map<String, Object>> errors = new ArrayList<>();
    private Map<String, Object> currentError;
    private final Map<String, Object> global = new LinkedHashMap<>();
    @Getter
    private final HttpStatus status;

    public ExceptionHandlingResponseBuilder(Exception e, HttpStatus status) {
        this.status = status;
        this.global.put(STATUS, status.value());
        this.global.put(TIMESTAMP, LocalDateTime.now());
        this.global.put(EXCEPTION, e.getClass().getName());
    }

    public ExceptionHandlingResponseBuilder put(String key, Object value) {
        if (this.currentError == null) {
            this.createNewError();
        }
        this.currentError.put(key, value);
        return this;
    }

    public ExceptionHandlingResponseBuilder newError() {
        this.createNewError();
        return this;
    }

    public ExceptionHandlingResponseBuilder globalInfo(String key, Object value) {
        this.global.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> errorMap = new LinkedHashMap<>(this.global);
        errorMap.put("errors", this.errors);
        return errorMap;
    }

    private void createNewError() {
        this.currentError = new LinkedHashMap<>();
        this.errors.add(this.currentError);
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }
}
