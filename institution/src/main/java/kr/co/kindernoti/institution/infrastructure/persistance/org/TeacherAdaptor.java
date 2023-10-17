package kr.co.kindernoti.institution.infrastructure.persistance.org;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.application.out.teacher.TeacherPort;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.mapper.TeacherMapper;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.QTeacherData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class TeacherAdaptor implements TeacherPort {

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    private final QTeacherData teacherData = QTeacherData.teacherData;

    @Override
    public Flux<Teacher> findByUserId(String userId) {
        BooleanExpression expression = teacherData.account.userId.eq(userId);
        return teacherRepository.findAll(expression)
                .map(teacherMapper::toDomain)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NoDataFoundException("userId",userId))));
    }

    @Override
    public Mono<Teacher> findInstitutionTeacher(InstitutionId institutionId, String userId) {
        BooleanExpression predicate = teacherData.institutionId.eq(institutionId)
                .and(teacherData.account.userId.eq(userId));
        return teacherRepository.findOne(predicate)
                .map(teacherMapper::toDomain);
    }

    @Override
    public Mono<Boolean> exist(InstitutionId institutionId, String userId) {
        BooleanExpression predicate = teacherData.institutionId.eq(institutionId)
                .and(teacherData.account.userId.eq(userId));
        return teacherRepository.exists(predicate);
    }

    @Override
    public Mono<Teacher> findById(TeacherId teacherId) {
        return teacherRepository.findById(teacherId)
                .map(teacherMapper::toDomain)
                .switchIfEmpty(Mono.defer(() ->
                        Mono.error(new NoDataFoundException("teacher", teacherId.toString()))));
    }

    @Override
    public Mono<Teacher> save(Teacher teacher) {
        return teacherRepository.save(teacherMapper.toData(teacher))
                .map(teacherMapper::toDomain);
    }

    @Override
    public Mono<TeacherId> delete(TeacherId teacherId) {
        return teacherRepository.deleteById(teacherId)
                .thenReturn(teacherId);
    }
}
