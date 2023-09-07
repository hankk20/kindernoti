package kr.co.kindernoti.auth.security.oauth;

import kr.co.kindernoti.auth.login.OauthProvider;
import org.springframework.security.oauth2.client.userinfo.DefaultReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class OauthUserService implements ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultReactiveOAuth2UserService userService = new DefaultReactiveOAuth2UserService();
    private final OauthUserConverterFactory oauthUserConverterFactory = new OauthUserConverterFactory();

    @Override
    public Mono<OAuth2User> loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return userService.loadUser(userRequest)
                .mapNotNull(oAuth2User -> {
                            OauthProvider oauthProvider = OauthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId());
                            return oauthUserConverterFactory.get(oauthProvider)
                                    .convert(oAuth2User);
                        });
    }

}
