package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import lombok.*;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

/**
 * 학년 정보
 * 학년에서 반{@link AClass}을 생성하거나 추가 할 수 있다.
 */
@Getter
public class Grade {

    private final GradeId id;

    private final InstitutionId institutionId;

    /**
     * 기준년월
     */
    private final String yearMonth;

    @Setter
    private String name;

    @Setter
    private String description;

    private List<AClass> aClasses = new ArrayList<>();

    @Builder(builderMethodName = "internalBuilder")
    private Grade(InstitutionId institutionId, String yearMonth, String name, String description) {
       this(institutionId, IdCreator.creator(GradeId.class).create(), yearMonth, name, description);
    }

    @Builder
    private Grade(InstitutionId institutionId, GradeId id, String yearMonth, String name, String description) {
        Validate.notNull(institutionId);
        Validate.notNull(id);
        Validate.notNull(yearMonth);
        Validate.notBlank(name);

        this.id = id;
        this.institutionId = institutionId;
        this.yearMonth = yearMonth;
        this.name = name;
        this.description = description;
    }

    public static GradeBuilder builder(InstitutionId institutionId, String yearMonth, String name) {
        return internalBuilder()
                .institutionId(institutionId)
                .yearMonth(yearMonth)
                .name(name);
    }

    public AClass createAClass(String name) {
        AClass aClass = new AClass(this.getId(), name);
        addAClass(aClass);
        return aClass;
    }

    public void addAClass(AClass aClass) {
        if(aClasses.stream()
                .anyMatch(aClass::isSameName)){
            throw new AlreadyDataException("동일한 이름의 반 정보가 존재 합니다. ["+aClass.getName()+"]");
        }
        aClasses.add(aClass);
    }
}
