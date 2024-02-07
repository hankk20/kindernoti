package kr.co.kindernoti.member.interfaces.rest.member;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import kr.co.kindernoti.core.spring.security.MemberAuthenticationToken;
import kr.co.kindernoti.member.application.in.AuthUseCase;
import kr.co.kindernoti.member.infrastructure.spring.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(value = MemberAuthController.class)
@AutoConfigureRestDocs
class MemberAuthControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    AuthUseCase authUseCase;

    @Test
    @DisplayName("회원에 기본 권한 부여")
    void testAddMemberRole() {
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ5aUJ1ekV5Tlh3bmN5TW4ycENTUkZDdzJOT01zSHFLcU81N0NUZ2tHR0VvIn0.eyJleHAiOjE3MDcyMjEyMDUsImlhdCI6MTcwNzIyMDkwNSwiYXV0aF90aW1lIjoxNzA3MjIwOTA0LCJqdGkiOiJjYzRhYzA2MS0wYmUzLTQ5ZDAtYTA2ZC02OGJhZjkyMGFjMGQiLCJpc3MiOiJodHRwOi8vYXV0aC5raW5kZXJub3RpLmNvLmtyL3JlYWxtcy9raW5kZXJub3RpIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImJhNzBmY2Q4LTdhNjUtNDk0OC1iYWRhLTZhNTk0MWU5OTQ4ZiIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlYWNoZXItY2xpZW50Iiwibm9uY2UiOiJyajhqTlhsTlAzZC1WQjF3blFaRlk3cDE3X0NsZTRUdWdvTVotUXFWVGo4Iiwic2Vzc2lvbl9zdGF0ZSI6ImU4MGEyZWI0LTk3YjMtNDIwYS1hZDUwLTAzY2UzMzQ2MzM4OCIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2tpbmRlcm5vdGkuY28ua3IiLCIvKiIsImh0dHA6Ly9sb2NhbGhvc3Q6OTAwMCJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiIsImRlZmF1bHQtcm9sZXMta2luZGVybm90aSJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJzaWQiOiJlODBhMmViNC05N2IzLTQyMGEtYWQ1MC0wM2NlMzM0NjMzODgiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6ImhhbmtrMjBAbmF2ZXIuY29tIiwiZW1haWwiOiJoYW5razIwQG5hdmVyLmNvbSJ9.wZN5DXdQh5DWrbVcpFrDH8JhVpdACIhFzG6yyr6R-lnEzUjOzfB8u9i4JwLxGq3e3aqDj75g9L9-5GouZToumoAjrrW2hKqaLMDS1r_ey7r0hogCAWZbNLMQRQw6U3AZKKMuTq2Rv_j_Fr8oORBYIWOVudvJ0LYMT4ysQjrkvEIFjFvVVJLUMsdnheu8In-eYfMs6-sO2Ba7_w7s12RSpSUwhbvjcsM5nUw3e5TJBYHlCUFVhpIg6rU9Wo_x8Jk5maTFEY419scTq8defiNDdxxk7oJ_uaF_hgFhnhL9Y9ycfjp8fqve_Xu-TecUB01mAHzhKIJlzzHQ2MvS2Pd9mA";
        given(authUseCase.addMemberRole(anyString(), anyString()))
                .willReturn(Mono.empty());

        client.mutateWith(SecurityMockServerConfigurers.mockAuthentication(new MemberAuthenticationToken("testid", "testname", "test@email.com", null)))
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .put()
                .uri("/member/roles")
                .headers(headerBuilder -> headerBuilder.setBearerAuth(jwt))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.headers(header -> header.setBearerAuth("jwt"))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(document("권한부여"
                        , resource(ResourceSnippetParameters.builder()
                                .summary("기본권한부여")
                                .description("회원가입후 가입한 서비스의 기본 권한을 부여 한다.")
                                .requestHeaders(headerWithName("Authorization").description("Bearer JWT"))
                                .build()
                        )
                    )
                );
    }

}