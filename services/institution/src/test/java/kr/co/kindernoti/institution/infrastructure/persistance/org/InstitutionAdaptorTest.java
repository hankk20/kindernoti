package kr.co.kindernoti.institution.infrastructure.persistance.org;

import kr.co.kindernoti.institution.application.out.org.InstitutionPort;
import kr.co.kindernoti.institution.application.out.org.InstitutionReadPort;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.InstitutionMapperImpl;
import kr.co.kindernoti.institution.testsupport.MongoContainerSupport;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Import({InstitutionAdaptor.class, InstitutionMapperImpl.class, InstitutionReadAdaptor.class})
class InstitutionAdaptorTest  extends MongoContainerSupport {

    @Autowired
    InstitutionPort institutionPort;

    @Autowired
    InstitutionReadPort institutionReadPort;

    @Test
    @DisplayName("Spring Repository Entity isNew에 따른 저장 업데이트 확인")
    void testEntityNew() {

        Institution institution = TestDataCreator.institutionDomain();
        StepVerifier.create(institutionPort.save(institution))
                .assertNext(s -> assertThat(s.getVersion()).isEqualTo(1))
                .verifyComplete();

        StepVerifier.create(institutionReadPort.findById(institution.getId()))
                .assertNext(s -> assertThat(s.getVersion()).isEqualTo(1))
                .verifyComplete();

        StepVerifier.create(institutionReadPort.findById(institution.getId())
                .flatMap(ins -> {
                    ins.setStatus(Status.REJECT);
                    return institutionPort.save(ins);
                }))
                .assertNext(r -> assertThat(r.getVersion()).isEqualTo(2))
                .verifyComplete();

    }


}