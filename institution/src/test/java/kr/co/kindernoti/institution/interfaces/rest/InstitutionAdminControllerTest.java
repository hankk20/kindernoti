package kr.co.kindernoti.institution.interfaces.rest;

import com.epages.restdocs.apispec.ConstrainedFields;
import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.org.InstitutionUseCase;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionCommand;
import kr.co.kindernoti.institution.interfaces.rest.mapper.InstitutionInterfaceMapper;
import kr.co.kindernoti.institution.interfaces.rest.organization.AdminInstitutionController;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
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

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static kr.co.kindernoti.institution.testsupport.OrganizationDocs.organizationResponseBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest({AdminInstitutionController.class, InstitutionInterfaceMapper.class})
@AutoConfigureRestDocs
class InstitutionAdminControllerTest {

    @Autowired
    WebTestClient client;

    @MockBean
    InstitutionUseCase institutionUseCase;

    @MockBean
    InstitutionSearchUseCase institutionSearchUseCase;

    @Test
    @DisplayName("관리자권한으로 기관 정보 수정")
    @WithMockUser(roles = "ADMIN")
    void testInstitutionUpdateForAdmin() {
        InstitutionId id = IdCreator.creator(InstitutionId.class).create();
        Address modifyAddress = Address.of("서울시 영등포구", "339");
        Status modifyStats = Status.APPROVAL;
        Institution testData = TestDataCreator.institutionDomain(id);
        testData.setStatus(Status.PENDING);

        Institution resultData = TestDataCreator.institutionDomain(id);
        resultData.setStatus(modifyStats);
        resultData.setAddress(modifyAddress);

        InstitutionCommand.PatchCommand patchCommand = InstitutionCommand.PatchCommand.of(modifyAddress, modifyStats);
        ConstrainedFields constrainedFields = new ConstrainedFields(InstitutionCommand.PatchCommand.class);

        //given
        given(institutionSearchUseCase.findById(any(InstitutionId.class)))
                .willReturn(Mono.just(testData));
        given(institutionUseCase.update(any(Institution.class)))
                .willReturn(Mono.just(resultData));

        client.mutateWith(SecurityMockServerConfigurers.csrf())
                .patch()
                .uri("/admin/institutions/{institutionId}", testData.getId().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(patchCommand)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("APPROVAL")
                .jsonPath("$.address.detail").isEqualTo(modifyAddress.getDetail())
                .jsonPath("$.address.street").isEqualTo(modifyAddress.getStreet())
                .consumeWith(document("기관 주소 및 상태 변경", resource(ResourceSnippetParameters.builder()
                        .summary("기관 정보 수정")
                        .description("기관의 주소 및 상태정보를 수정 한다.")
                        .requestSchema(Schema.schema("기관 정보 수정"))
                        .pathParameters(parameterWithName("institutionId").description("기관 아이디"))
                        .requestFields(constrainedFields.withPath("address.street").description("주소")
                                , constrainedFields.withPath("address.detail").description("상세 주소")
                                , new EnumFields(Status.class).withPath("status").description("상태"))
                        .responseSchema(organizationResponseBuilder().getResponseSchema())
                        .responseFields(organizationResponseBuilder().getResponseFields())
                        .build()
                )))
        ;


    }

}