package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.org.InstitutionType;
import kr.co.kindernoti.institution.domain.model.vo.Address;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("baseInstitution")
@TypeAlias("baseSchool")
public class BaseSchoolData extends BaseInstitutionData{


    /**
     * 초,중,고
     */
    private String schoolKind;

    private String dayType;

    public BaseSchoolData(String orgId, String name, String educationOfficeName, String educationSupportOfficeName, String foundationType, Address address, Phone telNo, String homepage, String schoolKind, String dayType) {
        super(orgId, name, educationOfficeName, educationSupportOfficeName, foundationType, address, telNo, homepage, InstitutionType.SCHOOL);
        this.schoolKind = schoolKind;
        this.dayType = dayType;
    }
}
