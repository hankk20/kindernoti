package kr.co.kindernoti.auth.security.oauth;

import kr.co.kindernoti.auth.login.OauthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.NoSuchElementException;

/**
 * OIDC 지원 하지 않는 Oatuh는 각 Provider 마다 사용자 정보 필드가 다르기 때문에 각 Provider 별로 LoginUser로 변환 하는 로직을 구현 해야 한다.
 */
public class OauthUserConverterFactory {
    public Converter<OAuth2User, OauthLoginUser> get(OauthProvider oauthProvider) {
        if(oauthProvider == OauthProvider.naver) {
            return (oauth) ->
                    OauthLoginUser.builder()
                            .userId(oauth.getName())
                            .email(oauth.getAttribute("email"))
                            .oauthProvider(oauthProvider)
                            .build();
        }
        throw new NoSuchElementException("["+oauthProvider+"] Not found Converter");
    }
}
