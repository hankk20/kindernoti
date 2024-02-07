package kr.co.kindernoti.auth.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.security.jwt.JwtProvider;
import kr.co.kindernoti.auth.security.oauth.OauthLoginUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = {JwtProvider.class, JwtConfiguration.class, JwtProperties.class, Jackson2ObjectMapperBuilder.class})
@EnableAutoConfiguration
public class JwtConfigurationTest {

    @Autowired JwtProvider jwtProvider;

    @Test
    @DisplayName("JWT 생성 검증")
    void rsaTest(){
        //given
        OauthLoginUser oauthLoginUser = testUser();
        //when
        String s = jwtProvider.create(oauthLoginUser);
        //then
        assertThat(s).isNotEmpty();
        boolean verify = jwtProvider.verify(s);
        assertThat(verify)
                .isTrue();
    }

    @Test
    @DisplayName("JWT 생성 검증 오류")
    void rsaTestFail() throws JOSEException {
        //given
        OauthLoginUser oauthLoginUser = testUser();
        RSAKey generate = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .generate();

        JwtProperties jwtProperties = Mockito.mock(JwtProperties.class);
        given(jwtProperties.getExpire())
                .willReturn(Duration.ofHours(1));
        given(jwtProperties.getIssuer())
                .willReturn("http://localhost:8080");

        JwtProvider jwtProvider1 = new JwtProvider(generate, jwtProperties, new Jackson2ObjectMapperBuilder());
        jwtProvider1.post();
        //when
        String s = jwtProvider1.create(oauthLoginUser);
        assertThat(s).isNotEmpty();

        //then
        boolean verify = jwtProvider.verify(s);
        assertThat(verify)
                .isFalse();

    }

    static OauthLoginUser testUser() {
        return OauthLoginUser.builder()
                .email("test")
                .userId("test@test.com")
                .oauthProvider(OauthProvider.google)
                .build();
    }

}
