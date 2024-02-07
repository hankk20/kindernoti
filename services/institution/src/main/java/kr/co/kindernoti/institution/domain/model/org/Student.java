package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.domain.model.vo.Id;

import java.time.LocalDate;

public class Student {
    private Id id;

    private String name;

    private LocalDate birthdate;

    private Parent parent;

    private AClass aClass;


}
