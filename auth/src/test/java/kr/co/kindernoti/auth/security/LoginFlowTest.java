package kr.co.kindernoti.auth.security;

import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import kr.co.kindernoti.auth.epages.EnumParameter;
import kr.co.kindernoti.auth.epages.ResponseJwtMockContentModifier;
import kr.co.kindernoti.auth.login.PlatformUser;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.jwt.JwtProvider;
import kr.co.kindernoti.auth.security.jwt.JwtService;
import kr.co.kindernoti.auth.security.oauth.OauthLoginSuccessHandler;
import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.security.platform.FormLoginUserDetailService;
import kr.co.kindernoti.auth.security.platform.PlatformLoginSuccessHandler;
import kr.co.kindernoti.auth.user.repository.OauthUserRepository;
import kr.co.kindernoti.auth.user.repository.PlatformUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({AuthSecurityConfiguration.class, PlatformLoginSuccessHandler.class, FormLoginUserDetailService.class, OauthLoginSuccessHandler.class, JwtService.class, ObjectMapper.class})
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8081)
class LoginFlowTest {

    @Autowired
    WebTestClient client;

    @MockBean
    OauthUserRepository oauthUserRepository;

    @MockBean
    PlatformUserRepository platformUserRepository;

    @MockBean
    JwtProvider jwtProvider;

    @Test
    @WithMockUser
    void testOauthLogin2() {
        client.get().uri("/login/oauth2/authorization/{provider}?service=parent", "kakao")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectBody()
                .consumeWith(document("Oauth Login",
                        preprocessResponse(new ResponseJwtMockContentModifier()),
                        ResourceDocumentation.resource(
                                ResourceSnippetParameters.builder()
                                        .summary("Oauth 인증 요청")
                                        .description("인증 제공자 정보와 serviceType 정보를 통해 Oauth 로그인 처리")
                                    .pathParameters(EnumParameter.of(OauthProvider.class).withName("provider")
                                            .description("인증 제공자"))
                                    .queryParameters(EnumParameter.of(List.of("parent", "teacher")).withName("service")
                                            .description("서비스 명"))
                                        .responseSchema(Schema.schema("Jwt 응답"))
                                        .build()
                        )
                        )
                );
    }

    @Test
    @DisplayName("Form 로그인")
    void testFormLogin() {
        //given
        String username = "testuser";
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String plainPassword = "1111";
        String encodedPassword = delegatingPasswordEncoder.encode(plainPassword);
        PlatformUser user = PlatformUser.builder()
                .userId(username)
                .password(encodedPassword)
                .email("test@test.com")
                .service(Set.of(ServiceType.parent))
                .build();
        given(platformUserRepository.findOne(any(Predicate.class)))
                .willReturn(Mono.just(user));
        given(jwtProvider.create(any()))
                .willReturn("jwt");



        client.post().uri("/login/form?service=parent")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username", "testuser")
                        .with("password", "1111")
                )
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("Form Login",
                                preprocessResponse(prettyPrint()),
                                ResourceDocumentation.resource(
                                        ResourceSnippetParameters.builder()
                                                .requestSchema(Schema.schema("로그인 Form"))
                                                .summary("Form Login")
                                                .description("사용자 아이디 / 패스워드로 인증 요청")
                                                .formParameters(parameterWithName("username")
                                                        .description("사용자 아이디"),
                                                        parameterWithName("password")
                                                                .description("사용자 패스워드")
                                                ).responseFields(fieldWithPath("token").description("JWT"))
                                                .responseSchema(Schema.schema("Jwt 응답"))
                                                .build()
                                )
                        )
                );
    }

    @Test
    @DisplayName("Form 로그인시 회원가입은 되어 있지만 서비스 가입은 안되어 있는 경우")
    void testFormLoginNotJoinedService() {
        //given
        String username = "testuser";
        PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String plainPassword = "1111";
        String encodedPassword = delegatingPasswordEncoder.encode(plainPassword);
        PlatformUser user = PlatformUser.builder()
                .userId(username)
                .password(encodedPassword)
                .email("test@test.com")
                .service(Set.of(ServiceType.parent))
                .build();
        given(platformUserRepository.findOne(any(Predicate.class)))
                .willReturn(Mono.just(user));
        given(platformUserRepository.save(any(PlatformUser.class)))
                .willReturn(Mono.just(user));
        given(jwtProvider.create(any()))
                .willReturn("jwt");

        client.post().uri("/login/form?service=teacher")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("username", "testuser")
                        .with("password", "1111")
                )
                .exchange()
                .expectStatus().isOk();
    }



}