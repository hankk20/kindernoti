package kr.co.kindernoti.auth.security;

import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.security.oauth.OauthLoginUser;
import kr.co.kindernoti.auth.security.oauth.OauthUserConverterFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.*;

public class OauthLoginUserTest {

    private static OAuth2User mock;
    private static OauthUserConverterFactory oauthUserConverterFactory;
    private static String name = "testId";
    private static String email = "test@test.com";

    @BeforeAll
    static void before() {
        //given
        oauthUserConverterFactory = new OauthUserConverterFactory();
        mock = mock(OAuth2User.class);
        given(mock.getName())
                .willReturn(name);
        given(mock.getAttribute(same("email")))
                .willReturn(email);
    }

    @Test
    @DisplayName("네이버 Oauth 객체를 LoginUser 객체로 변환 테스트")
    void oauthNaverToLoginUser() {
        //given
        OauthProvider oauthProvider = OauthProvider.naver;
        //when
        OauthLoginUser convert = oauthUserConverterFactory.get(oauthProvider)
                .convert(mock);
        //then
        assertThat(convert)
                .usingRecursiveComparison()
                .isEqualTo(OauthLoginUser.builder()
                        .userId(name)
                        .email(email)
                        .oauthProvider(OauthProvider.naver)
                        .build());

    }

    @Test
    @DisplayName("Oauth Converter가 없을때 오류 테스트")
    void oauthNaverToLoginUserThrows(){
        //given
        OauthProvider oauthProvider = OauthProvider.kakao;
        //when
        Throwable thrown = catchThrowable(() -> oauthUserConverterFactory.get(OauthProvider.kakao));
        //then
        assertThat(thrown)
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining(oauthProvider.toString());
    }


}
