package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.TeacherData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface TeacherRepository extends ReactiveMongoRepository<TeacherData, TeacherId>, ReactiveQuerydslPredicateExecutor<TeacherData>, CTeacherRepository {
}
