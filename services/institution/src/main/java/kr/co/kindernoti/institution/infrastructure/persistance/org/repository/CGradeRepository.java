package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.AClassData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.GradeData;
import reactor.core.publisher.Mono;

public interface CGradeRepository {
    Mono<GradeData> addAClass(GradeId gradeId, AClassData aClassData);
}
