package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.exception.InstitutionBusinessException;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.application.out.teacher.TeacherPort;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    TeacherUseCase teacherUseCase;

    @Mock
    TeacherPort teacherPort;

    @Mock
    InstitutionSearchService institutionSearchService;

    Institution institution;
    Teacher teacher;
    Account account;

    @BeforeEach
    void setUp() {
        teacherUseCase = new TeacherService(teacherPort, institutionSearchService, null, null);
        institution = TestDataCreator.institutionDomain();
        account = TestDataCreator.createAccount();
        teacher = new Teacher(institution.getId(), account);
    }
    @Test
    @DisplayName("회원가입")
    void testJoin() {

        given(institutionSearchService.findById(any(InstitutionId.class)))
                .willReturn(Mono.just(institution));
        given(teacherPort.exist(any(InstitutionId.class), anyString()))
                .willReturn(Mono.just(false));
        given(teacherPort.save(any(Teacher.class)))
                .willReturn(Mono.just(teacher));

        StepVerifier.create(teacherUseCase.join(institution.getId(), account))
                .assertNext(result -> assertThat(result).usingRecursiveComparison().isEqualTo(teacher))
                .verifyComplete();
    }

    @Test
    @DisplayName("회원 중복 가입")
    void testJoinAlready() {

        Teacher already = new Teacher(institution.getId(), account);
        already.setStatus(Status.APPROVAL);

        given(teacherPort.exist(any(InstitutionId.class), anyString()))
                .willReturn(Mono.just(true));

        StepVerifier.create(teacherUseCase.join(institution.getId(), account))
                .expectError(InstitutionBusinessException.class)
                .verify();
    }

}