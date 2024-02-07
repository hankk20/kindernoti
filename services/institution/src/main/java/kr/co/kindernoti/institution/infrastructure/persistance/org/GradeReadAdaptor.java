package kr.co.kindernoti.institution.infrastructure.persistance.org;

import kr.co.kindernoti.institution.application.out.org.GradeReadPort;
import kr.co.kindernoti.institution.domain.model.org.Grade;
import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.QGradeData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class GradeReadAdaptor implements GradeReadPort {

    @Override
    public Flux<Grade> getGradesByOrganizationId(InstitutionId id) {
        QGradeData gradeData = QGradeData.gradeData;
        return null;
    }

    @Override
    public Mono<Grade> findByGradeId(GradeId id) {
        return null;
    }
}
