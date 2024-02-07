package kr.co.kindernoti.institution.domain.model.vo;

import lombok.Getter;

@Getter
public class InvalidIdException extends RuntimeException{

    private final String idName;
    private final Object data;

    public InvalidIdException(String idName, Object data) {
        super("invalid Id format");
        this.idName = idName;
        this.data = data;
    }
}
