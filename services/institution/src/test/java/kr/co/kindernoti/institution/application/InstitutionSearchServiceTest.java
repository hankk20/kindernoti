package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.infrastructure.persistance.org.BaseInstitutionReadAdaptor;
import kr.co.kindernoti.institution.infrastructure.persistance.org.InstitutionReadAdaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class InstitutionSearchServiceTest {

    @Mock
    BaseInstitutionReadAdaptor commonOrganizationReadPort;

    @Mock
    InstitutionReadAdaptor organizationReadPort;

    @InjectMocks
    InstitutionSearchService service;

    @BeforeEach
    void setUp() {
        service = new InstitutionSearchService(commonOrganizationReadPort, organizationReadPort);
    }


    @Test
    @DisplayName("기관 정보를 우선하여 중복 제거")
    void testSearch() {
        given(organizationReadPort.search(anyString()))
                .willReturn(Flux.just(createTest("1", Status.REJECT)
                        , createTest("2", Status.REJECT)
                        , createTest("3", Status.REJECT))
                        .delaySubscription(Duration.ofSeconds(1)
                    ));
        given(commonOrganizationReadPort.search(anyString()))
                .willReturn(Flux.just(createTest("1", Status.EMPTY)
                        , createTest("4", Status.EMPTY)
                        , createTest("3", Status.EMPTY)
                        , createTest("5", Status.EMPTY)
                    ));

        StepVerifier.create(service.search(""))
                .expectNextMatches(s -> s.getOrgId().equals("1") && s.getStatus() == Status.REJECT)
                .expectNextMatches(s -> s.getOrgId().equals("2") && s.getStatus() == Status.REJECT)
                .expectNextMatches(s -> s.getOrgId().equals("3") && s.getStatus() == Status.REJECT)
                .expectNextMatches(s -> s.getOrgId().equals("4") && s.getStatus() == Status.EMPTY)
                .expectNextMatches(s -> s.getOrgId().equals("5") && s.getStatus() == Status.EMPTY)
                .verifyComplete();
    }

    InstitutionDto createTest(String id, Status status) {
        return InstitutionDto.builder()
                .orgId(id)
                .status(status)
                .build();
    }

}