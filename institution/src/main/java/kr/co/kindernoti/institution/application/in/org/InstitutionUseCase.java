package kr.co.kindernoti.institution.application.in.org;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import reactor.core.publisher.Mono;

public interface InstitutionUseCase {
    Mono<Institution> saveOrganization(String orgId);

    Mono<? extends Institution> changeStatus(InstitutionId id, Status status);

    Mono<Institution> save(Institution institution);
}
