package kr.co.kindernoti.member.interfaces.rest.member;

import com.epages.restdocs.apispec.ConstrainedFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.application.in.JoinUseCase;
import kr.co.kindernoti.member.interfaces.rest.JoinMapper;
import kr.co.kindernoti.member.interfaces.rest.dto.MemberCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.epages.restdocs.apispec.WebTestClientRestDocumentationWrapper.document;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

@WebFluxTest(value = {JoinController.class, JoinMapper.class})
@AutoConfigureRestDocs
class JoinControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    JoinUseCase joinUseCase;

    @Test
    @DisplayName("기본 회원 가입")
    @WithMockUser
    void testJoin() {
        MemberCommand.JoinCommand joinCommand = new MemberCommand.JoinCommand("testuser", "testpwd", "홍길동", "test@email.com");
        ConstrainedFields constrainedFields = new ConstrainedFields(MemberCommand.JoinCommand.class);
        given(joinUseCase.join(any(MemberJoinDto.class)))
                .willReturn(Mono.empty());

        client.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/join")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(joinCommand)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(
                        document("Member Join"
                                , resource(ResourceSnippetParameters.builder()
                                        .summary("공통 회원가입 API")
                                        .description("이 API를 통해 회원 가입을 한다.")
                                        .requestSchema(Schema.schema("회원가입 정보"))
                                        .requestFields(constrainedFields.withPath("username").description("회원 아이디")
                                                , constrainedFields.withPath("password").description("비밀번호")
                                                , constrainedFields.withPath("fullName").description("회원 이름")
                                                , constrainedFields.withPath("email").description("이메일")
                                        )
                                        .build())
                        )
                );
    }
}