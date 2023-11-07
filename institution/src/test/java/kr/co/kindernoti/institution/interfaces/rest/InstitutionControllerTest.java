package kr.co.kindernoti.institution.interfaces.rest;

import com.epages.restdocs.apispec.*;
import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.org.InstitutionUseCase;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionResponse;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import kr.co.kindernoti.institution.interfaces.rest.mapper.InstitutionInterfaceMapper;
import kr.co.kindernoti.institution.interfaces.rest.organization.InstitutionController;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({InstitutionController.class, InstitutionInterfaceMapper.class})
@AutoConfigureRestDocs
class InstitutionControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    InstitutionSearchUseCase institutionSearchUseCase;

    @MockBean
    InstitutionUseCase institutionUseCase;

    @Autowired
    InstitutionInterfaceMapper mapper;


    @Test
    @DisplayName("이름으로 기관 조회")
    @WithMockUser(roles = "TEACHER")
    void testSearch() {
        InstitutionDto institution = TestDataCreator.institutionDto("testOrg");
        InstitutionResponse response = mapper.toResponse(institution);

        given(institutionSearchUseCase.search(anyString()))
                .willReturn(Flux.just(institution));

        client.get()
                .uri("/institutions?name=딩동댕")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBodyList(InstitutionResponse.class)
                .value(result -> assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(response))
                .consumeWith(document("Organization Search"
                        , ResourceDocumentation.resource(ResourceSnippetParameters
                            .builder()
                            .summary("교육기관 조회")
                            .description("교육기관 명으로 교육기관 정보를 조회 한다.")
                            .queryParameters(parameterWithName("name").description("검색할 기관명"))
                                    .responseSchema(Schema.schema("기관정보"))
                                    .responseFields(fieldWithPath("[].id").description("교육기관 등록 아이디")
                                            , fieldWithPath("[].orgId").description("교육부의 교육기관 아이디")
                                            , fieldWithPath("[].name").description("교육기관명")
                                            , new EnumFields(InstitutionType.class).withPath("[].institutionType").description("교육기관 타입(유치원, 학교)")
                                            , new EnumFields(Status.class).withPath("[].status").description("기관 등록 상태")
                                            , fieldWithPath("[].address.street").description("주소")
                                            , fieldWithPath("[].address.detail").description("주소 상세")
                        ).build())));

    }

    @Test
    @WithMockUser("USER")
    @DisplayName("교육기관 등록")
    void testRegistryInstitution() {
        Institution institution = TestDataCreator.institutionDomain();

        InstitutionResponse response = mapper.toResponse(institution);
        given(institutionUseCase.saveInstitution(anyString()))
                .willReturn(Mono.just(institution));

        client.mutateWith(SecurityMockServerConfigurers.csrf())
                .post()
                .uri("/institutions/{orgId}", "testOrg")
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(InstitutionResponse.class)
                .value(s -> assertThat(s).usingRecursiveComparison().isEqualTo(response))
                .consumeWith(document("Institution Registry", ResourceDocumentation.resource(
                        ResourceSnippetParameters.builder()
                                .summary("교육기관을 시스템에 등록 한다.")
                                .pathParameters(parameterWithName("orgId").description("교육부의 교육기관 아이디"))
                                .responseSchema(Schema.schema("기관정보"))
                                .responseFields(fieldWithPath("id").description("교육기관 등록 아이디")
                                        , fieldWithPath("orgId").description("교육부의 교육기관 아이디")
                                        , fieldWithPath("name").description("교육기관명")
                                        , new EnumFields(InstitutionType.class).withPath("institutionType").description("교육기관 타입(유치원, 학교)")
                                        , new EnumFields(Status.class).withPath("status").description("기관 등록 상태")
                                        , fieldWithPath("address.street").description("주소")
                                        , fieldWithPath("address.detail").description("주소 상세"))
                                .build()
                )));
    }

}