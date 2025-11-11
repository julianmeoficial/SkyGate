package com.skygate.backend.exception;

public class InvalidFlightDataException extends RuntimeException {

    private String fieldName;
    private Object fieldValue;

    public InvalidFlightDataException(String message) {
        super(message);
    }

    public InvalidFlightDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFlightDataException(String fieldName, Object fieldValue) {
        super(String.format("Invalid value for field %s: %s", fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public InvalidFlightDataException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
