package kr.co.kindernoti.member.infrastructure.spring.security;

import kr.co.kindernoti.member.infrastructure.api.keycloak.AccessToken;
import kr.co.kindernoti.member.infrastructure.api.keycloak.KeycloakAdminRestClient;
import kr.co.kindernoti.member.infrastructure.api.keycloak.KeycloakProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * ID/PW 로그인 인증
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakUserAuthenticationManager implements ReactiveAuthenticationManager {

    private final KeycloakAdminRestClient adminRestClient;
    private final KeycloakProperties keycloakProperties;

    /**
     * Keycloak 서버로 ID/PW를 전달하여 인증 토큰을 받아서 리턴한다.
     * @param authentication the {@link Authentication} to test
     * @return
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String name = authentication.getName();
        Object credentials = authentication.getCredentials();
        return adminRestClient.accessToken(keycloakProperties.getRealm(), adminRestClient.createRequestTokenBody(name, credentials.toString()))
                .map(this::create)
                .cast(Authentication.class)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BadCredentialsException("Invalid Credentials"))));
    }

    public UsernamePasswordJwtToken create(AccessToken token) {
        return new UsernamePasswordJwtToken(token.getAccessToken(), token.getRefreshToken());
    }
}
