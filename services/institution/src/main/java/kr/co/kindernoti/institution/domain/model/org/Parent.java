package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.domain.model.vo.Id;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.Getter;

@Getter
public class Parent {

    private Id id;

    private String name;

    private Phone phone;

}
