package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.jwt.JwtProvider;
import kr.co.kindernoti.auth.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@SpringBootTest(classes = UserTokenService.class)
public class UserTokenServiceTest {

    @Autowired
    private UserTokenService userTokenService;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private UserRepository userRepository;
    @Test
    @DisplayName("JWT 갱신")
    void createToken() {
        User user = new User("test", "test@test.com", Set.of(ServiceType.teacher));
        when(userRepository.findById(anyString()))
                .thenReturn(Mono.just(user));
        when(jwtProvider.create(any()))
                .thenReturn("jwttoken");

        StepVerifier.create(userTokenService.getJwt(user.getUserId()))
                .assertNext(s -> assertThat(s).isNotEmpty())
                .verifyComplete();


    }

}
