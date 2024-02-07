package kr.co.kindernoti.member.infrastructure.spring.security;

import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * Keycloak 인증서버의 인증제공자 선택 Hint Parameter 처리
 */
@Component
public class KeycloakOauth2AuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    private final DefaultServerOAuth2AuthorizationRequestResolver authorizationRequestResolver;

    public static final String DEFAULT_IDP_HIND = "kc_idp_hint";



    public KeycloakOauth2AuthorizationRequestResolver(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.authorizationRequestResolver = new DefaultServerOAuth2AuthorizationRequestResolver(clientRegistrationRepository);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        String hint = exchange.getRequest().getQueryParams()
                .toSingleValueMap()
                .get(DEFAULT_IDP_HIND);

        return authorizationRequestResolver.resolve(exchange)
                .map(request -> OAuth2AuthorizationRequest.from(request)
                        .parameters(param -> param.put(DEFAULT_IDP_HIND, hint))
                        .build());
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        return authorizationRequestResolver.resolve(exchange, clientRegistrationId);
    }
}
