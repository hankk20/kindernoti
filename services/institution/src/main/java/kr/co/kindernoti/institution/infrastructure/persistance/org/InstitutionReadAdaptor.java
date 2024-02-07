package kr.co.kindernoti.institution.infrastructure.persistance.org;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.application.out.org.InstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.InstitutionMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.QInstitutionData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 시스템에 등록된 학교 및 유치원의 정보를 조회한다.
 */
@RequiredArgsConstructor
@Component
public class InstitutionReadAdaptor implements InstitutionReadPort {

    private final InstitutionRepository repository;

    private final InstitutionMapper mapper;

    @Override
    public Flux<InstitutionDto> search(String name) {
        BooleanExpression like = QInstitutionData.institutionData.name.like(name);
        return repository.findAll(like)
                .map(mapper::toDto);
    }

    @Override
    public Mono<InstitutionDto> findByOrgId(String orgId) {
        return repository.findOne(QInstitutionData.institutionData.orgId.eq(orgId))
                .map(mapper::toDto);
    }

    @Override
    public Mono<Institution> findById(InstitutionId id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .switchIfEmpty(Mono.error(new NoDataFoundException("Institution", id.toString())));
    }

    @Override
    public Mono<Boolean> existByOrgId(String orgId) {
        return repository.findOne(QInstitutionData.institutionData.orgId.eq(orgId))
                .map(org -> true)
                .switchIfEmpty(Mono.just(false));
    }

    public static void main(String[] args) {
        System.out.println(IdCreator.creator(InstitutionId.class).create().toString());
    }
}
