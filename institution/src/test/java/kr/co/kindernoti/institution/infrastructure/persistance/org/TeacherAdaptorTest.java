package kr.co.kindernoti.institution.infrastructure.persistance.org;

import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.application.out.teacher.TeacherPort;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.TeacherMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.TeacherMapperImpl;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.TeacherData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.TeacherRepository;
import kr.co.kindernoti.institution.testsupport.MongoContainerSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({TeacherMapperImpl.class, TeacherAdaptor.class})
class TeacherAdaptorTest extends MongoContainerSupport {

    @Autowired
    TeacherPort teacherPort;

    @Autowired
    TeacherRepository repository;

    @Test
    @DisplayName("저장 테스트")
    void testSave() {
        Teacher teacher = createTestTeacherData();

        StepVerifier.create(teacherPort.save(teacher))
                .assertNext(t -> assertThat(t).usingRecursiveComparison().isEqualTo(teacher))
                .verifyComplete();

        StepVerifier.create(teacherPort.findById(teacher.getId()))
                .assertNext(t -> assertThat(t).usingRecursiveComparison().isEqualTo(teacher))
                .verifyComplete();

    }

    @Test
    @DisplayName("존재하지 않은 정보 삭제")
    void testDelete() {
        TeacherId teacherId = IdCreator.creator(TeacherId.class).create();
        StepVerifier.create(teacherPort.delete(teacherId))
                .expectNext(teacherId)
                .verifyComplete();
    }

    @Test
    @DisplayName("존재하지 않은 정보 조회")
    void testNoData() {
        StepVerifier.create(teacherPort.findById(IdCreator.creator(TeacherId.class).create()))
                .expectError(NoDataFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("계정정보 수정")
    void testModifyAccount() {
        Teacher teacher = createTestTeacherData();
        Account account = teacher.getAccount();
        Account updateAccount = Account.of(account.getUserId(), account.getName(), account.getEmail(), Phone.of("01033334444", Phone.PhoneType.MOBILE), List.of("ROLE_TEACHER", "SCOPE_READ_GRADE"));

        StepVerifier.create(teacherPort.save(teacher)
                    .flatMap(result -> teacherPort.updateAccount(result.getId(), updateAccount)))
                .assertNext(updated -> assertThat(updated.getAccount()).usingRecursiveComparison().isEqualTo(updateAccount))
                .verifyComplete();
    }

    @Test
    void testRepositoryUpdate() {
        Teacher teacher = createTestTeacherData();
        Account account = teacher.getAccount();
        Account updateAccount = Account.of(account.getUserId(), account.getName(), account.getEmail(), Phone.of("01033334444", Phone.PhoneType.MOBILE), List.of("ROLE_TEACHER", "SCOPE_READ_GRADE"));
        TeacherMapper mapper = Mappers.getMapper(TeacherMapper.class);
        TeacherData data = mapper.toData(teacher);

        StepVerifier.create(repository.save(data)
                        .flatMap(result -> {
                            return repository.findById(result.getId())
                                            .flatMap(find -> {
                                                find.setAccount(updateAccount);
                                                return repository.save(find);
                                                    });

                        }))
                .assertNext(updated -> assertThat(updated.getAccount()).usingRecursiveComparison().isEqualTo(updateAccount))
                .verifyComplete();
    }

    private Teacher createTestTeacherData() {
        Account account = Account.of("testuser1", "홍길동", "test@test.com", Phone.of("01011112222", Phone.PhoneType.MOBILE), List.of("ROLE_TEACHER"));
        return new Teacher(IdCreator.creator(InstitutionId.class).create(), account);
    }

}