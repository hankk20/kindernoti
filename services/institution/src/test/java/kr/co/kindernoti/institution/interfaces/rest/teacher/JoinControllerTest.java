package kr.co.kindernoti.institution.interfaces.rest.teacher;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.core.spring.security.MemberAuthenticationToken;
import kr.co.kindernoti.institution.application.dto.TeacherDto;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import kr.co.kindernoti.institution.domain.model.vo.Status;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({JoinController.class, TeacherInterfaceMapper.class})
@AutoConfigureRestDocs
class JoinControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    TeacherUseCase teacherUseCase;

    @SneakyThrows
    @Test
    @WithMockUser(username = "testUserId")
    @DisplayName("회원 가입")
    void testJoin() {
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

        client.mutateWith(SecurityMockServerConfigurers.mockAuthentication(new MemberAuthenticationToken(account.getUserId(), account.getName(), account.getEmail(), null)))
                .mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                    .uri("/institution/{institutionId}/teachers", institutionId.toString())
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer jwt")
                .exchange()
                .expectStatus().isOk()
                .expectBody(TeacherDto.class)
                .value(result -> assertThat(result).usingRecursiveComparison().isEqualTo(teacherDto))
                .consumeWith(document("회원 가입", resource(ResourceSnippetParameters.builder()
                        .summary("해당 기관 아이디에 회원을 가입을 진행한다.")
                        .description("헤더 사용자 계정정보를 사용하여 해당 기관으로 회원 가입을 진행 한다.")
                        .pathParameters(parameterWithName("institutionId").description("기관 아이디"))
                        .requestHeaders(headerWithName("Authorization").description("Bearer JWT"))
                        .responseSchema(Schema.schema("회원 정보"))
                        .responseFields(fieldWithPath("id").description("아이디")
                                , fieldWithPath("institutionId").description("소속 기관 아이디")
                                , new EnumFields(Status.class).withPath("status").description("회원가입 상태")
                                , fieldWithPath("account.userId").description("계정 아이디")
                                , fieldWithPath("account.name").description("사용자 이름")
                                , fieldWithPath("account.email").description("이메일")
                                , fieldWithPath("account.phone.number").description("전화번호")
                                , new EnumFields(Phone.PhoneType.class).withPath("account.phone.phoneType").description("전화번호 유형")
                        )
                        .build()
                )));


    }

}