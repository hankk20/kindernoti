package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Document("baseInstitution")
@TypeAlias("baseKindergartenData")
public class BaseKindergartenData extends BaseInstitutionData{

    /**
     * 대표자
     */
    private String representName;

    /**
     * 원장
     */
    private String directorName;

    /**
     * 설립일
     */
    private LocalDate foundationDate;

    /**
     * 개원일
     */
    private LocalDate openDate;

    public BaseKindergartenData(String orgId, String name, String educationOfficeName, String educationSupportOfficeName, String foundationType, Address address, Phone telNo, String homepage, String representName, String directorName, LocalDate foundationDate, LocalDate openDate) {
        super(orgId, name, educationOfficeName, educationSupportOfficeName, foundationType, address, telNo, homepage, InstitutionType.KINDERGARTEN);
        this.representName = representName;
        this.directorName = directorName;
        this.foundationDate = foundationDate;
        this.openDate = openDate;
    }
}
