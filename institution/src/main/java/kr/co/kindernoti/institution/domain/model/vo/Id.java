package kr.co.kindernoti.institution.domain.model.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Id {

    private UUID id;

    protected Id(){}

    public String toString() {
        return id.toString();
    }

    public static Id from(UUID id){
        return new Id(id);
    }
    public static Id from(String id) {
        return new Id(UUID.fromString(id));
    }

    public static Id create() {
        return new Id(UUID.randomUUID());
    }
}
