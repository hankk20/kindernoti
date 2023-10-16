package kr.co.kindernoti.institution.application.dto;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class InstitutionDto {

    private InstitutionId id;

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

    @Builder.Default
    private Status status = Status.EMPTY;




}
