package kr.co.kindernoti.institution.domain.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;

@Getter
@EqualsAndHashCode
public class PlainId implements Serializable {

    @EqualsAndHashCode.Include
    private final UUID uuid;

    public PlainId(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
