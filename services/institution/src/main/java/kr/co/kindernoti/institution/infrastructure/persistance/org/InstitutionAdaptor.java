package kr.co.kindernoti.institution.infrastructure.persistance.org;

import kr.co.kindernoti.institution.application.out.org.InstitutionPort;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.InstitutionMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class InstitutionAdaptor implements InstitutionPort {

    private final InstitutionRepository institutionRepository;

    private final InstitutionMapper mapper;

    @Override
    public Mono<Institution> save(Institution institution) {
        return institutionRepository.save(mapper.toData(institution))
                .map(mapper::toDomain);
    }
}
