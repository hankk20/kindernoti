package kr.co.kindernoti.auth.user.repository;

import com.querydsl.core.types.Predicate;
import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.user.specification.OauthUserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataMongoTest
class OauthUserRepositoryTest {

    @Autowired
    OauthUserRepository oauthUserRepository;

    @Autowired
    PlatformUserRepository platformUserRepository;



    @Test
    void test(){
        Predicate predicate = OauthUserSpecification.builder()
                .userId("")
                .oauthProvider(OauthProvider.kakao)
                .build()
                .toPredicate();
        StepVerifier.create(oauthUserRepository.findOne(predicate))
                .expectSubscription()
                .verifyComplete();
    }

}