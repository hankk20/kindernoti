package kr.co.kindernoti.institution.interfaces.rest.dto;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class InstitutionResponse {

    private String id;

    private String orgId;

    /**
     * 기관 명
     */
    private String name;

    /**
     * 주소
     */
    private Address address;

    private InstitutionType institutionType;

    @Builder.Default
    private Status status = Status.EMPTY;

}


