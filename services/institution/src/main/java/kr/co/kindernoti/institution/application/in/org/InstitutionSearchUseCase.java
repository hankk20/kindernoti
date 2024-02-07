package kr.co.kindernoti.institution.application.in.org;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InstitutionSearchUseCase {
    Flux<InstitutionDto> search(String name);

    Mono<Institution> findById(InstitutionId id);
}
