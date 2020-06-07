package org.adrian.mypetclinic.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionAttributes {
    public static final String MESSAGE = "message";
    public static final String PROPERTY_PATH = "propertyPath";
    public static final String INVALID_VALUE = "invalidValue";
    public static final String MESSAGE_TEMPLATE = "messageTemplate";
    public static final String TIMESTAMP = "timestamp";
    public static final String EXCEPTION = "exception";
    public static final String STATUS = "status";
}
