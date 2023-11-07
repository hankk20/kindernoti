package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.domain.model.DomainModel;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Institution extends DomainModel {

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

    public Institution(InstitutionId id, String name, String orgId, Address address, InstitutionType institutionType) {
        this(id, name, orgId, address, institutionType, 0);
    }

    @Builder
    public Institution(InstitutionId id, String name, String orgId, Address address, InstitutionType institutionType, int version) {
        super(version);
        this.id = id;
        this.orgId = orgId;
        this.name = name;
        this.address = address;
        this.status = Status.PENDING;
        this.institutionType = institutionType;
    }

}
