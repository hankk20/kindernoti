package kr.co.kindernoti.institution.testsupport;

import com.epages.restdocs.apispec.EnumFields;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class OrganizationDocs {

    private static ResourceSnippetParameters commonOrganizationResponseParameter;
    public static ResourceSnippetParameters organizationResponseBuilder() {
        if(commonOrganizationResponseParameter == null) {
            commonOrganizationResponseParameter = ResourceSnippetParameters.builder()
                    .responseSchema(Schema.schema("기관정보"))
                    .responseFields(fieldWithPath("id").description("교육기관 등록 아이디")
                            , fieldWithPath("orgId").description("교육부의 교육기관 아이디")
                            , fieldWithPath("name").description("교육기관명")
                            , new EnumFields(InstitutionType.class).withPath("institutionType").description("교육기관 타입(유치원, 학교)")
                            , new EnumFields(Status.class).withPath("status").description("기관 등록 상태")
                            , fieldWithPath("address.street").description("주소")
                            , fieldWithPath("address.detail").description("주소 상세")
                    ).build();
        }
        return commonOrganizationResponseParameter;
    }

}
