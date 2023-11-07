package kr.co.kindernoti.institution.interfaces.rest.teacher;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.infrastructure.spring.security.BeanConfig;
import kr.co.kindernoti.institution.infrastructure.spring.security.HeaderAuthenticationConverter;
import kr.co.kindernoti.institution.infrastructure.spring.security.InstitutionSecurity;
import kr.co.kindernoti.institution.interfaces.rest.mapper.TeacherInterfaceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({InstitutionSecurity.class, BeanConfig.class, HeaderAuthenticationConverter.class, ObjectMapper.class, TeacherController.class, TeacherInterfaceMapper.class})
@AutoConfigureRestDocs
class TeacherControllerTest {

    @Autowired
    WebTestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TeacherUseCase useCase;

    @Test
    @DisplayName("내정보 조회")
    void test() throws JsonProcessingException {
        //given
        InstitutionId institutionId = IdCreator.creator(InstitutionId.class).create();
        Account account = Account.of("test", "홍길동", "ttt@ttt.com",Phone.of("01012223333", Phone.PhoneType.MOBILE), List.of("ROLE_TEACHER"));
        Teacher teacher = new Teacher(institutionId
                , account);

        given(useCase.findTeacherByUserId((anyString())))
                .willReturn(Flux.just(teacher));
        String s = objectMapper.writeValueAsString(account);


        //then
        client.get().uri("/me")
                    .header("x-auth", s)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                    .jsonPath("$.[0].id").isEqualTo(teacher.getId().toString())
                    .jsonPath("$.[0].institutionId").isEqualTo(institutionId.toString())
                    .jsonPath("$.[0].account.userId").isEqualTo("test")
                    .jsonPath("$.[0].status").isEqualTo("PENDING")
                    .jsonPath("$.[0].account.name").isEqualTo("홍길동")
                    .jsonPath("$.[0].account.email").isEqualTo("ttt@ttt.com")
                    .jsonPath("$.[0].account.phone.number").isEqualTo("01012223333")
                    .jsonPath("$.[0].account.authorities.[0]").isEqualTo("ROLE_TEACHER")
                .consumeWith(document("내 정보 조회", resource(ResourceSnippetParameters.builder()
                        .summary("내 정보 조회")
                        .description("로그인 사용자의 정보를 조회한다.")
                        .requestHeaders(headerWithName("x-auth").description("사용자 인증정보"))
                        .responseSchema(Schema.schema("내 정보"))
                        .responseFields(fieldWithPath("[].id").description("아이디")
                                , fieldWithPath("[].institutionId").description("소속 기관 아이디")
                                , new EnumFields(Status.class).withPath("[].status").description("회원가입 상태")
                                , fieldWithPath("[].account.userId").description("계정 아이디")
                                , fieldWithPath("[].account.name").description("사용자 이름")
                                , fieldWithPath("[].account.email").description("이메일")
                                , fieldWithPath("[].account.phone.number").description("전화번호")
                                , new EnumFields(Phone.PhoneType.class).withPath("[].account.phone.phoneType").description("전화번호 유형")
                                , fieldWithPath("[].account.authorities").description("사용자 권한")
                                )
                        .build())));
    }


}