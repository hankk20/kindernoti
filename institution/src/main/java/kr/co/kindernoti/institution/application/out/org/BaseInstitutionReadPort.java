package kr.co.kindernoti.institution.application.out.org;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 기초 기관 데이터와 기관 데이터 조회 공통 Port
 */
public interface BaseInstitutionReadPort {

    Flux<InstitutionDto> search(String name);

    Mono<InstitutionDto> findByOrgId(String orgId);

}
