package kr.co.kindernoti.institution.infrastructure.external.api.education;

import kr.co.kindernoti.institution.infrastructure.persistance.org.model.BaseInstitutionData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.BaseInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * API를 통해 받아온 교육기관 정보를 저장한다.
 */
@RequiredArgsConstructor
@Service
public class BaseInstitutionService {

    private final BaseInstitutionRepository baseInstitutionRepository;

    public Mono<BaseInstitutionData> save(BaseInstitutionData data) {
        return baseInstitutionRepository.save(data);
    }
}
