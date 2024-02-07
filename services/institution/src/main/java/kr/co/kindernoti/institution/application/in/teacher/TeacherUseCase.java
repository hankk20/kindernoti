package kr.co.kindernoti.institution.application.in.teacher;

import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TeacherUseCase {
    Flux<Teacher> findTeacherByUserId(String userId);

    Mono<Teacher> save(Teacher teacher);

    Mono<Teacher> findById(TeacherId teacherId);

    Mono<Teacher> join(InstitutionId id, Account account);

    Mono<Void> updateAccount(TeacherId id, Account account);
}
