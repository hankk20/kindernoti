package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("baseInstitution")
public class BaseInstitutionData {

    @Id
    private String orgId;

    /**
     * 기관 명
     */
    private String name;
    /**
     * 교육청명
     */
    private String educationOfficeName;

    /**
     * 교육지원청명
     * 교육청의 하위 기관
     */
    private String educationSupportOfficeName;

    /**
     * 설립유형
     * enum으로 관리할지 추후 결정
     */
    private String foundationType;

    /**
     * 주소
     */
    private Address address;

    /**
     * 전화번호
     */
    private Phone telNo;

    /**
     * 홈페이지
     */
    private String homepage;

    private InstitutionType institutionType;

    public BaseInstitutionData(String orgId, String name, String educationOfficeName, String educationSupportOfficeName, String foundationType, Address address, Phone telNo, String homepage, InstitutionType institutionType) {
        this.orgId = orgId;
        this.name = name;
        this.educationOfficeName = educationOfficeName;
        this.educationSupportOfficeName = educationSupportOfficeName;
        this.foundationType = foundationType;
        this.address = address;
        this.telNo = telNo;
        this.homepage = homepage;
        this.institutionType = institutionType;
    }
}
