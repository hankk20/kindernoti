package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.GradeId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.GradeData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface GradeRepository extends ReactiveMongoRepository<GradeData, GradeId>, ReactiveQuerydslPredicateExecutor<GradeData>, CGradeRepository {
}
