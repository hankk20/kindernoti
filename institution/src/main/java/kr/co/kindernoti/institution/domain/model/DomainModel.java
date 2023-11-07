package kr.co.kindernoti.institution.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class DomainModel {

    @Setter
    private int version;

    public DomainModel(int version) {
        this.version = version;
    }
}
