package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.out.org.BaseInstitutionReadPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 기관 조회 서비스
 */
@RequiredArgsConstructor
@Service
public class InstitutionSearchService implements InstitutionSearchUseCase {

    private final BaseInstitutionReadPort baseInstitutionReadPort;
    private final InstitutionReadPort institutionReadPort;

    /**
     * 기관을 검색 한다
     * concatWith를 사용하여 등록된 기관을 우선 검색후 기초기관 데이터를 검색 하여 중복 기관은 제거후 리턴 한다.
     * @param name
     * @return
     */
    @Override
    public Flux<InstitutionDto> search(String name) {
        return institutionReadPort.search(name)
                .concatWith(baseInstitutionReadPort.search(name))
                .distinct(InstitutionDto::getOrgId);
    }

    @Override
    public Mono<Institution> findById(InstitutionId id) {
        return institutionReadPort.findById(id);
    }
}
