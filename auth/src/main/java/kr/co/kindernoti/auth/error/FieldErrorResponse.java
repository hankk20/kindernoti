package kr.co.kindernoti.auth.error;

import lombok.Getter;

@Getter
public class FieldErrorResponse {
    private final String fieldName;
    private final String message;

    private FieldErrorResponse(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }

    public static FieldErrorResponse of(String fieldName, String message) {
        return new FieldErrorResponse(fieldName, message);
    }

}
