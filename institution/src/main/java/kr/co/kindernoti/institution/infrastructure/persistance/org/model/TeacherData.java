package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.infrastructure.persistance.shared.AuditMetadata;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document("teacher")
@TypeAlias("teacher")
public class TeacherData extends AuditMetadata {

    @EqualsAndHashCode.Include
    private final TeacherId id;

    /**
     * 회원 계정 정보
     */
    private final Account account;

    @Setter
    private InstitutionId institutionId;

    @Setter
    private Status status;

    @Version
    private int version;

    @Builder
    public TeacherData(TeacherId id, InstitutionId institutionId, Account account) {
        Validate.notNull(id);
        Validate.notNull(institutionId);
        Validate.notNull(account);
        this.institutionId = institutionId;
        this.id = id;
        this.account = account;
    }
}
