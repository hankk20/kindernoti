package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.security.jwt.JwtProvider;
import kr.co.kindernoti.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserTokenService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public Mono<String> getJwt(String userId) {
        return userRepository.findById(userId)
                .flatMap(u -> Mono.just(jwtProvider.create(u)))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("")));
    }
}
