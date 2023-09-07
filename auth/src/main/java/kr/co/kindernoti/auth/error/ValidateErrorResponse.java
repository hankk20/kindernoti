package kr.co.kindernoti.auth.error;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidateErrorResponse {

    private final int status = 400;

    private final String title = "입력 값이 올바르지 않습니다.";

    private final List<FieldErrorResponse> invalidParams;

    public ValidateErrorResponse(List<FieldErrorResponse> invalidParams) {
        this.invalidParams = invalidParams;
    }
}
