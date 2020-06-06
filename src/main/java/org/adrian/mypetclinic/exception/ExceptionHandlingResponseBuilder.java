package org.adrian.mypetclinic.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ExceptionHandlingResponseBuilder {
    private final List<Map<String, Object>> errors = new ArrayList<>();
    private Map<String, Object> currentError;
    private final Map<String, Object> global = new LinkedHashMap<>();

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
