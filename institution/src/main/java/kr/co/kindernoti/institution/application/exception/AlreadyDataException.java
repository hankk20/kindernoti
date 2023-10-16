package kr.co.kindernoti.institution.application.exception;

public class AlreadyDataException extends RuntimeException{
    private static final String MESSAGE_TEMPLATE = "이미 존재하는 데이터 입니다. [%s]";

    public AlreadyDataException(Object data) {
        super(MESSAGE_TEMPLATE.formatted(data));
    }
}
