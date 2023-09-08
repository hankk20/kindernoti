package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.user.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final PlatformUserRepository platformUserRepository;

    public Mono<PlatformUser> join(PlatformUser platformUser) {
        encodePassword(platformUser);
        return platformUserRepository.save(platformUser)
                .log()
                .doOnError((e) -> log.error("신규 사용자 저장 오류", e))
                .onErrorMap(DuplicateKeyException.class
                        , e -> new ErrorResponseException(HttpStatus.BAD_REQUEST
                                , ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "동일한 아이디가 존재 합니다."), e)
                );
    }

    private void encodePassword(PlatformUser user) {
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.changePassword(delegatingPasswordEncoder.encode(user.getPassword()));
    }

}
