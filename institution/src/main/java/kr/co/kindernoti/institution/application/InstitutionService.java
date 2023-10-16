package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.application.in.org.InstitutionUseCase;
import kr.co.kindernoti.institution.application.out.org.BaseInstitutionReadPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class InstitutionService implements InstitutionUseCase {

    private final InstitutionReadPort institutionReadPort;

    private final BaseInstitutionReadPort baseInstitutionReadPort;

    private final InstitutionPort institutionPort;


    /**
     * 교육청에서 관리하는 기관 아이디로 등록된 기관이 있는지 확인 하여 등록된 기관이 없는 경우에만 신규로 등록 한다.
     * @param orgId 교육청에서 관리하는 기관 아이디
     * @return 등록된 기관 정보
     * @throws AlreadyDataException 이미 등록된 기관인 경우 오류 발생
     */
    @Override
    public Mono<Institution> saveOrganization(String orgId) {
        return institutionReadPort.existByOrgId(orgId)
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.error(new AlreadyDataException(orgId));
                    }
                    return baseInstitutionReadPort.findByOrgId(orgId)
                            .flatMap(org -> {
                                Institution institution = new Institution(IdCreator.creator(InstitutionId.class).create(), org.getName(), org.getOrgId(), org.getAddress(), org.getInstitutionType());
                                return institutionPort.save(institution);
                            });
                });
    }

    /**
     * 기관의 상태를 변경한다.
     * @param id
     * @param status
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Mono<Institution> changeStatus(InstitutionId id, Status status) {
        return institutionReadPort.findById(id)
                .flatMap(org -> {
                    org.setStatus(status);
                    return institutionPort.save(org);
                }).switchIfEmpty(Mono.error(new NoDataFoundException("Institution",id)));
    }

    @Override
    public Mono<Institution> save(Institution institution) {
        return institutionPort.save(institution);
    }


}
