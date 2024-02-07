package kr.co.kindernoti.institution.infrastructure.persistance.org.repository;

import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.InstitutionData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface InstitutionRepository extends ReactiveMongoRepository<InstitutionData, InstitutionId>, ReactiveQuerydslPredicateExecutor<InstitutionData>, COrganizationRepository {
}
