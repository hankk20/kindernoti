package kr.co.kindernoti.auth.user.repository;

import kr.co.kindernoti.auth.user.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;

public interface UserRepository extends ReactiveMongoRepository<User, String>, ReactiveQuerydslPredicateExecutor<User> {
}
