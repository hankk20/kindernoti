package kr.co.kindernoti.institution.interfaces.rest.teacher;

import com.epages.restdocs.apispec.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kindernoti.institution.application.dto.TeacherDto;
import kr.co.kindernoti.institution.application.in.org.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.vo.*;
import kr.co.kindernoti.institution.infrastructure.spring.security.BeanConfig;
import kr.co.kindernoti.institution.infrastructure.spring.security.HeaderAuthenticationConverter;
import kr.co.kindernoti.institution.infrastructure.spring.security.InstitutionSecurity;
import kr.co.kindernoti.institution.infrastructure.spring.web.ApplicationXHeaderNames;
import kr.co.kindernoti.institution.interfaces.rest.mapper.TeacherInterfaceMapper;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({InstitutionSecurity.class, BeanConfig.class, HeaderAuthenticationConverter.class, ObjectMapper.class, JoinController.class, TeacherInterfaceMapper.class})
@AutoConfigureRestDocs
class JoinControllerTest {

    @Autowired
    WebTestClient client;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TeacherUseCase teacherUseCase;

    @SneakyThrows
    @Test
    @DisplayName("회원 가입")
    void test() {
        //given
        InstitutionId institutionId = IdCreator.creator(InstitutionId.class).create();
        Account account = TestDataCreator.createAccount();
        Teacher teacher = new Teacher(institutionId
                , account);

        given(teacherUseCase.join(any(InstitutionId.class), any(Account.class)))
                .willReturn(Mono.just(teacher));

        TeacherDto teacherDto = TeacherDto.builder().id(teacher.getId().toString())
                .institutionId(teacher.getInstitutionId().toString())
                .account(account)
                .status(teacher.getStatus())
                .build();

        client.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                    .uri("/join/institution/{institutionId}", institutionId.toString())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("x-auth", objectMapper.writeValueAsString(account))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeacherDto.class)
                .value(result -> assertThat(result).usingRecursiveComparison().isEqualTo(teacherDto))
                .consumeWith(document("회원 가입", resource(ResourceSnippetParameters.builder()
                        .summary("해당 기관 아이디에 회원을 가입을 진행한다.")
                        .description("헤더 사용자 계정정보를 사용하여 해당 기관으로 회원 가입을 진행 한다.")
                        .pathParameters(parameterWithName("institutionId").description("기관 아이디"))
                        .requestHeaders(headerWithName(ApplicationXHeaderNames.AUTHORIZE_HEADER).description("사용자 인증정보"))
                        .responseSchema(Schema.schema("회원 정보"))
                        .responseFields(fieldWithPath("id").description("아이디")
                                , fieldWithPath("institutionId").description("소속 기관 아이디")
                                , new EnumFields(Status.class).withPath("status").description("회원가입 상태")
                                , fieldWithPath("account.userId").description("계정 아이디")
                                , fieldWithPath("account.name").description("사용자 이름")
                                , fieldWithPath("account.email").description("이메일")
                                , fieldWithPath("account.phone.number").description("전화번호")
                                , new EnumFields(Phone.PhoneType.class).withPath("account.phone.phoneType").description("전화번호 유형")
                                , fieldWithPath("account.roles").description("사용자 권한")
                        )
                        .build()
                )));


    }

}