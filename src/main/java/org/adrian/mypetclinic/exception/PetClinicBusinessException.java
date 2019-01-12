package org.adrian.mypetclinic.exception;

import lombok.Getter;

public class PetClinicBusinessException extends RuntimeException {

    @Getter
    private String propertyPath;
    @Getter
    private String invalidValue;
    @Getter
    private String messageTemplate;

    public PetClinicBusinessException(Builder builder) {
        super(builder.message, builder.cause);
        this.propertyPath = builder.propertyPath;
        this.invalidValue = builder.invalidValue;
        this.messageTemplate = builder.messageTemplate;
    }

    public static class Builder {
        private final String message;
        private Throwable cause;
        private String propertyPath;
        private String invalidValue;
        private String messageTemplate;

        public Builder(String message) {
            this.message = message;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder propertyPath(String propertyPath) {
            this.propertyPath = propertyPath;
            return this;
        }

        public Builder invalidValue(String invalidValue) {
            this.invalidValue = invalidValue;
            return this;
        }
        public Builder messageTemplate(String messageTemplate) {
            this.messageTemplate = messageTemplate;
            return this;
        }

        public PetClinicBusinessException build() {
            return new PetClinicBusinessException(this);
        }
    }

}
