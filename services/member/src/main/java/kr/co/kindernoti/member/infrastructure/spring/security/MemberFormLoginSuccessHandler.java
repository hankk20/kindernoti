package kr.co.kindernoti.member.infrastructure.spring.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Form Login 성공시 Token을 리턴한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MemberFormLoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final AccessTokenWriter accessTokenWriter;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        if(authentication instanceof UsernamePasswordJwtToken usernamePasswordJwtToken) {
            return Mono.defer(() -> accessTokenWriter.writeAccessToken(webFilterExchange.getExchange()
                    , new AccessTokenWriter.AccessTokenValue(usernamePasswordJwtToken.getAccessToken()
                            , usernamePasswordJwtToken.getRefreshToken())));
        }
        return Mono.empty();
    }


}
