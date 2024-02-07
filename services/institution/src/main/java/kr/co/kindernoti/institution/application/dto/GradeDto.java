package kr.co.kindernoti.institution.application.dto;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import lombok.Value;

@Value(staticConstructor = "of")
public class GradeDto {

    GradeId id;

    InstitutionId institutionId;

    String yearMonth;

    String name;

    String description;


}
