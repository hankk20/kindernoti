package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.application.out.org.BaseInstitutionReadPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    BaseInstitutionReadPort baseInstitutionReadPort;

    @Mock
    InstitutionReadPort institutionReadPort;

    @Mock
    InstitutionPort institutionPort;

    InstitutionService organizationService;

    InstitutionDto testDto;

    Institution testDomain;
    @BeforeEach
    void setUp() {
        testDto = TestDataCreator.institutionDto("testOrgId");
        testDomain = TestDataCreator.organizationDomainFromDto(testDto);
        organizationService = new InstitutionService(institutionReadPort, baseInstitutionReadPort, institutionPort);
    }

    @Test
    @DisplayName("기관 저장")
    void testSave() {
        //given
        given(institutionReadPort.existByOrgId(anyString()))
                .willReturn(Mono.just(false));
        given(baseInstitutionReadPort.findByOrgId(anyString()))
                .willReturn(Mono.just(testDto));
        given(institutionPort.save(any(Institution.class)))
                .willReturn(Mono.just(testDomain));
        //when
        Mono<Institution> organizationMono = organizationService.saveOrganization(testDomain.getOrgId());

        //then
        StepVerifier.create(organizationMono)
                .assertNext(s -> assertThat(s).isEqualTo(testDomain))
                .verifyComplete();

    }

    @Test
    @DisplayName("이미 등록된 기관 오류")
    void testSaveAlreadyException() {
        //given
        given(institutionReadPort.existByOrgId(anyString()))
                .willReturn(Mono.just(true));

        //when
        Mono<Institution> organizationMono = organizationService.saveOrganization("");

        //then
        StepVerifier.create(organizationMono)
                .verifyError(AlreadyDataException.class);

    }

    @Test
    @DisplayName("기관의 상태를 PENDING에서 APPROVAL로 변경")
    void testStatusChange(){
        //given
        given(institutionReadPort.findById(any()))
                .willReturn(Mono.just(testDomain));

        given(institutionPort.save(any(Institution.class)))
                .willReturn(Mono.defer(() -> {
                    testDomain.setStatus(Status.APPROVAL);
                    return Mono.just(testDomain);
                }));

        //when
        Mono<Institution> organizationMono = organizationService.changeStatus(IdCreator.creator(InstitutionId.class).create(), Status.APPROVAL);

        //then
        StepVerifier.create(organizationMono)
                .assertNext(org -> assertThat(org.getStatus())
                        .isEqualTo(Status.APPROVAL))
                .verifyComplete();

    }
}