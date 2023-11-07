package kr.co.kindernoti.institution.application.in.org;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import reactor.core.publisher.Mono;

public interface InstitutionUseCase {

    /**
     * 기관정보 저장
     * @param orgId
     * @return
     */
    Mono<Institution> saveInstitution(String orgId);

    /**
     * 기관 상태 변경
     * @param id
     * @param status
     * @return
     */
    Mono<? extends Institution> changeStatus(InstitutionId id, Status status);

    /**
     * 기관정보를 수정한다.
     * @param institution
     * @return
     */
    Mono<Institution> update(Institution institution);
}
