package kr.co.kindernoti.institution.application.out.teacher;

import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeacherPort {
    Flux<Teacher> findByUserId(String userId);

    Mono<Teacher> findInstitutionTeacher(InstitutionId institutionId, String userId);

    Mono<Boolean> exist(InstitutionId institutionId, String userId);

    Mono<Teacher> findById(TeacherId teacherId);

    Mono<Teacher> save(Teacher teacher);

    Mono<TeacherId> delete(TeacherId teacherId);
}
