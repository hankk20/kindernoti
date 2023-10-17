package kr.co.kindernoti.institution.infrastructure.persistance.org;

import kr.co.kindernoti.institution.application.out.org.GradePort;
import kr.co.kindernoti.institution.domain.model.org.Grade;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GradeAdaptor implements GradePort {
    @Override
    public Mono<Grade> save(Grade grade) {
        return null;
    }
}
