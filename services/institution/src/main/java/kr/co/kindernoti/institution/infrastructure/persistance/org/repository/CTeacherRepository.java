package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.TeacherData;
import reactor.core.publisher.Mono;

public interface CTeacherRepository {
    Mono<TeacherData> updateAccount(TeacherId id, Account account);
}
