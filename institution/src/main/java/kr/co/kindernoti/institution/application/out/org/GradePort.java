package kr.co.kindernoti.institution.application.out.org;

import kr.co.kindernoti.institution.domain.model.org.Grade;
import reactor.core.publisher.Mono;

public interface GradePort {

    Mono<Grade> save(Grade grade);

}
