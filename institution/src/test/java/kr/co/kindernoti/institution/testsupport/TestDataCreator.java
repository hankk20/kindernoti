package kr.co.kindernoti.institution.testsupport;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.*;

import java.util.List;

public class TestDataCreator {

    public static InstitutionDto institutionDto(String orgId) {
        return InstitutionDto.builder()
                .id(IdCreator.creator(InstitutionId.class).create())
                .orgId(orgId)
                .name("딩동댕유치원")
                .institutionType(InstitutionType.KINDERGARTEN)
                .status(Status.EMPTY)
                .telNo(Phone.of("0211112222", Phone.PhoneType.TEL))
                .address(Address.of("서울시 영등포구 여의도동 여의나루로", "31"))
                .build();
    }

    public static Institution organizationDomainFromDto(InstitutionDto dto) {
        return createInstitutionDomain(IdCreator.creator(InstitutionId.class).create(), dto.getName(), dto.getOrgId(), dto.getAddress(), dto.getInstitutionType());
    }

    public static Institution institutionDomain() {
        return institutionDomain(IdCreator.creator(InstitutionId.class).create());
    }

    public static Institution institutionDomain(InstitutionId id) {
        return createInstitutionDomain(id, "테스트 학교", "testOrgId", Address.of("서울시 영등포구 여의도동 여의나루로", "31"), InstitutionType.SCHOOL);
    }

    private static Institution createInstitutionDomain(InstitutionId id, String name, String orgId, Address address, InstitutionType institutionType) {
        return new Institution(id, name, orgId, address, institutionType);
    }

    public static Account createAccount() {
        return Account.of("testUserId", "홍길동", "test@ttt.com",Phone.of("01011112222", Phone.PhoneType.MOBILE), List.of(Role.TEACHER));
    }
}
