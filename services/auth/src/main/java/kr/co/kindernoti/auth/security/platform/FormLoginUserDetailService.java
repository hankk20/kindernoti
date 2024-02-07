package kr.co.kindernoti.auth.security.platform;

import kr.co.kindernoti.auth.security.DefaultAuthorities;
import kr.co.kindernoti.auth.user.PlatformUser;
import kr.co.kindernoti.auth.user.QPlatformUser;
import kr.co.kindernoti.auth.user.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class FormLoginUserDetailService implements ReactiveUserDetailsService {

    private final PlatformUserRepository platformUserRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return platformUserRepository.findOne(QPlatformUser.platformUser.userId.eq(username))
                .map(this::convert)
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new UsernameNotFoundException("회원정보가 없습니다"))));
    }

    private UserDetails convert(PlatformUser platformUser) {
        return PlatformLoginUser.customBuilder()
                .userId(platformUser.getUserId())
                .password(platformUser.getPassword())
                .services(platformUser.getServiceTypes())
                .email(platformUser.getEmail())
                .authorities(DefaultAuthorities.get())
                .build();
    }
}
