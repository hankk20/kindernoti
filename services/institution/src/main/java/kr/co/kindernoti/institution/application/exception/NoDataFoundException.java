package kr.co.kindernoti.institution.application.exception;

import lombok.Getter;

@Getter
public class NoDataFoundException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "No Data Found %s";

    private String dataName;
    private Object data;

    public NoDataFoundException(String dataName) {
        super(MESSAGE_TEMPLATE.formatted(dataName));
    }

    public NoDataFoundException(String dataName, Object data) {
        super(MESSAGE_TEMPLATE.formatted(dataName));
        this.dataName = dataName;
        this.data = data;
    }

}
