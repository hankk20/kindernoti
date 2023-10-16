package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.infrastructure.persistance.org.model.BaseInstitutionData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

import java.util.UUID;

public interface BaseInstitutionRepository extends ReactiveMongoRepository<BaseInstitutionData, String>, ReactiveQuerydslPredicateExecutor<BaseInstitutionData> {
}
