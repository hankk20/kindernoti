package kr.co.kindernoti.institution.infrastructure.basedata;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CityCodeRepository extends ReactiveMongoRepository<CityCode, String> {
}
