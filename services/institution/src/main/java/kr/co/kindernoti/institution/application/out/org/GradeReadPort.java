package kr.co.kindernoti.institution.application.out.org;

import kr.co.kindernoti.institution.domain.model.org.Grade;
import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GradeReadPort {
    Flux<Grade> getGradesByOrganizationId(InstitutionId id);

    Mono<Grade> findByGradeId(GradeId id);
}
