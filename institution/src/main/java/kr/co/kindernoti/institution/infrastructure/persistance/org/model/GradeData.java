package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.shared.AuditMetadata;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * 학년 정보
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Document("grade")
@TypeAlias("grade")
public class GradeData extends AuditMetadata {

    @EqualsAndHashCode.Include
    @Id
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

    private List<AClassData> aClasses;

    @Version
    private int version;

    @Builder
    public GradeData(GradeId id, InstitutionId institutionId, String yearMonth, String name, String description, List<AClassData> aClasses) {
        this.id = id;
        this.institutionId = institutionId;
        this.yearMonth = yearMonth;
        this.name = name;
        this.description = description;
        this.aClasses = aClasses;
    }
}
