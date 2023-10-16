package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.infrastructure.persistance.shared.AuditMetadata;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Document("organization")
@TypeAlias("organization")
public class InstitutionData extends AuditMetadata {

    @Id
    private InstitutionId id;

    private String orgId;

    private String name;

    private Address address;

    private Status status;

    private InstitutionType institutionType;


    @Version
    private int version;

    public InstitutionData(InstitutionId id, String orgId, String name, Address address, InstitutionType institutionType) {
        this.id = id;
        this.orgId = orgId;
        this.name = name;
        this.address = address;
        this.status = Status.PENDING;
        this.institutionType = institutionType;
    }
}
