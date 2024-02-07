package kr.co.kindernoti.auth.security.oauth;

import kr.co.kindernoti.auth.login.OauthProvider;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Spring Security OidcUser를 {@link OauthLoginUser} 로 변환 한다
 */
@Component
public class OidcUserService implements ReactiveOAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcReactiveOAuth2UserService oidcReactiveOAuth2UserService = new OidcReactiveOAuth2UserService();

    @Override
    public Mono<OidcUser> loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        return oidcReactiveOAuth2UserService.loadUser(userRequest)
                .map(oidcUser -> {
                    OauthProvider oauthProvider = OauthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId());
                    return OauthLoginUser.builder()
                            .userId(oidcUser.getName())
                            .email(oidcUser.getEmail())
                            .oauthProvider(oauthProvider)
                            .build();
                });
    }

}
