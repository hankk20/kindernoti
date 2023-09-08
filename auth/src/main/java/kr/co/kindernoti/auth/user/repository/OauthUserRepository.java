package kr.co.kindernoti.auth.user.repository;

import kr.co.kindernoti.auth.user.OauthUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface OauthUserRepository extends ReactiveMongoRepository<OauthUser, String>, ReactiveQuerydslPredicateExecutor<OauthUser> {
}
