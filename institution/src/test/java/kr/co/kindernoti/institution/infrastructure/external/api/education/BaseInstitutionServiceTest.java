package kr.co.kindernoti.institution.infrastructure.external.api.education;

import kr.co.kindernoti.institution.application.out.org.BaseInstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import kr.co.kindernoti.institution.infrastructure.persistance.org.BaseInstitutionReadAdaptor;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.BaseInstitutionMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.BaseKindergartenData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.BaseSchoolData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.BaseInstitutionRepository;
import kr.co.kindernoti.institution.testsupport.MongoContainerSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class BaseInstitutionServiceTest extends MongoContainerSupport {


    @Autowired
    BaseInstitutionRepository baseInstitutionRepository;

    BaseInstitutionService baseInstitutionService;

    BaseInstitutionReadPort baseInstitutionReadPort;


    @BeforeEach
    void setUp() {
        baseInstitutionService = new BaseInstitutionService(baseInstitutionRepository);
        baseInstitutionReadPort = new BaseInstitutionReadAdaptor(baseInstitutionRepository, Mappers.getMapper(BaseInstitutionMapper.class));
    }

    @Test
    @DisplayName("기본 저장 테스트")
    void testSave() {
        BaseKindergartenData baseKindergartenData = new BaseKindergartenData( "testOrgId","동네유치원", "서울교육청", "서울교육지원청", "공립(병설)"
                , Address.of("강원특별자치도 강릉시 백두대간로", "2011-8"), Phone.of("033-648-6131", Phone.PhoneType.TEL)
                , "", "홍홍호", "홍이홍", LocalDate.of(1936, 8, 4), LocalDate.of(1936, 8, 4));

        StepVerifier.create(baseInstitutionService.save(baseKindergartenData))
                .assertNext(data -> assertThat(data.getOrgId())
                        .isNotEmpty())
                .verifyComplete();

        StepVerifier.create(baseInstitutionRepository.deleteById(baseKindergartenData.getOrgId()))
                .expectSubscription()
                .verifyComplete();



        BaseSchoolData baseSchoolData = new BaseSchoolData("schoolId", "동네유치원", "서울교육청", "서울교육지원청", "공립(병설)"
                , Address.of("강원특별자치도 강릉시 백두대간로", "2011-8"), Phone.of("033-648-6131", Phone.PhoneType.TEL)
                , "", "초등학교", "11");

        StepVerifier.create(baseInstitutionService.save(baseSchoolData))
                .assertNext(data -> assertThat(data.getOrgId())
                        .isNotEmpty())
                .verifyComplete();

        StepVerifier.create(baseInstitutionRepository.deleteById(baseSchoolData.getOrgId()))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("조회 테스트")
    void testGet() {
        BaseKindergartenData baseKindergartenData = new BaseKindergartenData( "testOrgId","동네유치원", "서울교육청", "서울교육지원청", "공립(병설)"
                , Address.of("강원특별자치도 강릉시 백두대간로", "2011-8"), Phone.of("033-648-6131", Phone.PhoneType.TEL)
                , "", "홍홍호", "홍이홍", LocalDate.of(1936, 8, 4), LocalDate.of(1936, 8, 4));

        StepVerifier.create(baseInstitutionService.save(baseKindergartenData)
                        .then(Mono.defer(() -> baseInstitutionReadPort.findByOrgId("testOrgId")))
                )
                .assertNext(s -> {
                    assertThat(s.getName()).isEqualTo(baseKindergartenData.getName());
                    assertThat(s.getOrgId()).isEqualTo(baseKindergartenData.getOrgId());
                })
                .verifyComplete();

        StepVerifier.create(baseInstitutionRepository.deleteById(baseKindergartenData.getOrgId()))
                .expectSubscription()
                .verifyComplete();
    }
}