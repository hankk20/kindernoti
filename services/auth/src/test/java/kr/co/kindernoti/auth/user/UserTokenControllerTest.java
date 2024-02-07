package kr.co.kindernoti.auth.user;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceDocumentation;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.auth.epages.ConstrainedFieldsExtends;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.DefaultAuthorities;
import kr.co.kindernoti.auth.user.JoinController;
import kr.co.kindernoti.auth.user.JoinRequest;
import kr.co.kindernoti.auth.user.PlatformUser;
import kr.co.kindernoti.auth.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(value = UserTokenController.class)
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8081)
class UserTokenControllerTest {

    @Autowired
    UserTokenService joinController;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    UserTokenService userTokenService;
    @Test
    @DisplayName("갱신된 토큰 받기")
    @WithMockUser(roles = DefaultAuthorities.DEFAULT_AUTHORITY, username = "test")
    void test() {
        //given

        when(userTokenService.getJwt(anyString()))
                .thenReturn(Mono.just("jwt sample"));

        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
                .get()
                .uri("/token")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("token"
                        , ResourceDocumentation.resource(ResourceSnippetParameters
                                .builder().requestSchema(Schema.schema("토큰 갱신"))
                                .responseFields(fieldWithPath("token").description("갱신된 JWT"))
                                .build()
                        )
                ));
    }

}