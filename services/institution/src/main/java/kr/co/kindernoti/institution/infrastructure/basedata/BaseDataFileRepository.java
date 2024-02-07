package kr.co.kindernoti.institution.infrastructure.basedata;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface BaseDataFileRepository extends ReactiveMongoRepository<BaseDataFile, String>, ReactiveQuerydslPredicateExecutor<BaseDataFile> {

}
