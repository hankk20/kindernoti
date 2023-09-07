package kr.co.kindernoti.auth.user;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.auth.epages.ConstrainedFieldsExtends;
import kr.co.kindernoti.auth.login.PlatformUser;
import kr.co.kindernoti.auth.login.ServiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(value = JoinController.class)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8081)
class JoinControllerTest {

    @Autowired
    JoinController joinController;

    @MockBean
    UserService userService;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    @WithMockUser(roles = "anonymous")
    void test() {
        //given
        JoinRequest joinRequest = JoinRequest.builder()
                .userId("testPuser")
                .email("email@e.com")
                .password("1111")
                .serviceType(ServiceType.parent)
                .build();
        ConstrainedFieldsExtends fields = new ConstrainedFieldsExtends(JoinRequest.class);
        //when
        when(userService.join(any(PlatformUser.class)))
                .thenReturn(Mono.just(joinRequest.toPlatformUser()));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(joinRequest), JoinRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("join"
                        , ResourceDocumentation.resource(ResourceSnippetParameters
                                .builder().requestSchema(Schema.schema("회원가입"))
                                .requestFields(fields.withPath("userId").description("로그인 아이디")
                                        , fields.withPath("password").description("로그인 비밀번호")
                                        , fields.withPath("email").description("이메일")
                                        , new EnumFields(ServiceType.class).withPath("serviceType").description("회원가입 서비스명")
                                ).build()
                        )
                ));
    }

}