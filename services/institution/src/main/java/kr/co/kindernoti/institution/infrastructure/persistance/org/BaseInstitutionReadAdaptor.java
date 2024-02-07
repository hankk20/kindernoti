package kr.co.kindernoti.institution.infrastructure.persistance.org;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.application.out.org.BaseInstitutionReadPort;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.BaseInstitutionMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.QInstitutionData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.BaseInstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 교육부의 기준 데이터 기반의 학교 및 유치원 기본정보 조회
 */
@RequiredArgsConstructor
@Component
public class BaseInstitutionReadAdaptor implements BaseInstitutionReadPort {

    private final BaseInstitutionRepository baseInstitutionRepository;

    private final BaseInstitutionMapper mapper;

    @Override
    public Flux<InstitutionDto> search(String name) {
        BooleanExpression expression = QInstitutionData.institutionData.name.like(name);
        return baseInstitutionRepository.findAll(expression)
                .map(mapper::toDto);
    }

    @Override
    public Mono<InstitutionDto> findByOrgId(String orgId) {
        BooleanExpression expression = QInstitutionData.institutionData.orgId.eq(orgId);
        return baseInstitutionRepository
                .findOne(expression)
                .map(mapper::toDto);
    }
}
