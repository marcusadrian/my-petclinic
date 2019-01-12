package org.adrian.mypetclinic.exception;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class ExceptionHandlingResponseBuilder {
    private List<Map<String, Object>> errors = new ArrayList<>();
    private Map<String, Object> currentError;
    private Map<String, Object> global = new LinkedHashMap<>();

    public ExceptionHandlingResponseBuilder put(String key, Object value) {
        if (this.currentError == null) {
            createNewError();
        }
        this.currentError.put(key, value);
        return this;
    }

    public ExceptionHandlingResponseBuilder newError() {
        createNewError();
        return this;
    }

    public ExceptionHandlingResponseBuilder globalInfo(String key, Object value) {
        this.global.put(key, value);
        return this;
    }

    public Map<String, Object> getErrorMap() {
        Map<String, Object> errorMap = new LinkedHashMap<>(global);
        errorMap.put("errors", errors);
        return errorMap;
    }

    private void createNewError() {
        this.currentError = new LinkedHashMap<>();
        this.errors.add(this.currentError);
    }

}
