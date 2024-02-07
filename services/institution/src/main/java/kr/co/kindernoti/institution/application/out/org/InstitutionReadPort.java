package kr.co.kindernoti.institution.application.out.org;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InstitutionReadPort {

    Flux<InstitutionDto> search(String name);

    Mono<InstitutionDto> findByOrgId(String orgId);

    Mono<Institution> findById(InstitutionId id);

    Mono<Boolean> existByOrgId(String id);
}
