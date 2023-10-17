package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.out.org.GradePort;
import kr.co.kindernoti.institution.application.out.org.GradeReadPort;
import kr.co.kindernoti.institution.domain.model.org.Grade;
import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GradeService {

    private final GradeReadPort gradeReadPort;
    private final GradePort gradePort;

    public Flux<Grade> getGrades(InstitutionId institutionId){
        return gradeReadPort.getGradesByOrganizationId(institutionId);
    }

    public Mono<Grade> findById(GradeId gradeId) {
        return gradeReadPort.findByGradeId(gradeId);
    }

    public Mono<Grade> save(Grade grade) {
        return gradePort.save(grade);
    }

    public Mono<Grade> createAClass(GradeId gradeId, String className) {
        return gradeReadPort.findByGradeId(gradeId)
                .flatMap(grade -> {
                    grade.createAClass(className);
                    return gradePort.save(grade);
                });
    }
}
