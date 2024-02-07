package kr.co.kindernoti.member.infrastructure.spring.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

@Slf4j
public class OauthLoginFailHandler implements ServerAuthenticationFailureHandler {

    private final RedirectServerAuthenticationFailureHandler handler;

    public OauthLoginFailHandler() {
        this.handler = new RedirectServerAuthenticationFailureHandler("/login?error");
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.error("[Oauth Login] Failure", exception);
        return handler.onAuthenticationFailure(webFilterExchange, exception);
    }
}
