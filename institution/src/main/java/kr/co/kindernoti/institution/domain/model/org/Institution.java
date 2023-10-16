package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Institution {

    //Entity ID
    private final InstitutionId id;

    //학교 및 기관 아이디
    private final String orgId;

    private final String name;

    private final InstitutionType institutionType;

    @Setter
    private Address address;

    @Setter
    private Status status;

    @Builder
    public Institution(InstitutionId id, String name, String orgId, Address address, InstitutionType institutionType) {
        this.id = id;
        this.orgId = orgId;
        this.name = name;
        this.address = address;
        this.status = Status.PENDING;
        this.institutionType = institutionType;
    }


}
