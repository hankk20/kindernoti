package kr.co.kindernoti.institution.application.out.org;

import kr.co.kindernoti.institution.domain.model.org.Institution;
import reactor.core.publisher.Mono;

public interface InstitutionPort {
    Mono<Institution> save(Institution institution);

}
