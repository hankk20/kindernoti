package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.GradeData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.InstitutionData;
import reactor.core.publisher.Mono;

public interface COrganizationRepository {
    Mono<InstitutionData> addGrade(InstitutionId id, GradeData gradeData);

    Mono<GradeData>  findByGradeId(InstitutionId id, GradeId gradeId);

    Mono<InstitutionData> findByOrganizationId(InstitutionId id, GradeId gradeId);

}
