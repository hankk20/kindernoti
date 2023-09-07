package kr.co.kindernoti.core.user;

import kr.co.kindernoti.parent.user.User;
import kr.co.kindernoti.parent.user.UserType;
import kr.co.kindernoti.parent.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void test() {
        User user = new User("홍길동", "01011112222", UserType.PARENT);
        StepVerifier.create(userRepository.save(user))
                .assertNext((u) -> assertThat(u.getId()).isNotNull())
                .verifyComplete();

        StepVerifier.create(userRepository.findById(user.getId()))
                .assertNext(u -> assertThat(u.getName()).isEqualTo(user.getName()))
                .verifyComplete();
    }
}
