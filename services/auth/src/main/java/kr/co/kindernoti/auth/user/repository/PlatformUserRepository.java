package kr.co.kindernoti.auth.user.repository;

import kr.co.kindernoti.auth.user.PlatformUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface PlatformUserRepository extends ReactiveMongoRepository<PlatformUser, String>, ReactiveQuerydslPredicateExecutor<PlatformUser> {
}
