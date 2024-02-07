package kr.co.kindernoti.member.infrastructure.spring.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Oauth Login 성공시 Token을 리턴한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MemberOauthLoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final AccessTokenWriter accessTokenWriter;
    private final ReactiveOAuth2AuthorizedClientService authorizedClientService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        if(authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            return authorizedClientService.loadAuthorizedClient(authorizedClientRegistrationId, authentication.getName())
                    .map(oauthClient -> new AccessTokenWriter.AccessTokenValue(oauthClient.getAccessToken().getTokenValue()
                                    ,oauthClient.getRefreshToken().getTokenValue()))
                    .flatMap(token -> accessTokenWriter.writeAccessToken(webFilterExchange.getExchange(), token));
        }
        return Mono.empty();
    }


}
