package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.user.repository.PlatformUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.ErrorResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.*;


@SpringBootTest(classes = UserService.class)
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    PlatformUserRepository platformUserRepository;

    @Test
    void joinTest() {
        JoinRequest request = JoinRequest.builder()
                .userId("puser")
                .email("puser@test.com")
                .password("11111")
                .serviceType(ServiceType.parent)
                .build();

        when(platformUserRepository.save(any(PlatformUser.class)))
                .thenReturn(Mono.just(request.toPlatformUser()));

        StepVerifier.create(service.join(request.toPlatformUser()))
                .expectSubscription()
                .assertNext(s -> s.equals(request.toPlatformUser()))
                .verifyComplete();
    }

    @Test
    @DisplayName("아이디 중복 오류 확인")
    void joinUserIdDup(){
        JoinRequest request = JoinRequest.builder()
                .userId("puser")
                .email("puser@test.com")
                .password("11111")
                .serviceType(ServiceType.parent)
                .build();

        when(platformUserRepository.save(any(PlatformUser.class)))
                .thenReturn(Mono.error(new DuplicateKeyException("")));

        StepVerifier.create(service.join(request.toPlatformUser()))
                .expectError(ErrorResponseException.class)
                .verify();
    }


}