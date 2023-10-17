package kr.co.kindernoti.institution.domain.model.teacher;

import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Teacher {

    @EqualsAndHashCode.Include
    @Builder.Default
    private final TeacherId id;

    /**
     * 회원 계정 정보
     */
    private final Account account;

    private final InstitutionId institutionId;

    @Setter
    private Status status;

    @Builder
    public Teacher(InstitutionId institutionId, TeacherId id, Account account) {
        Validate.notNull(account);
        Validate.notNull(id);
        this.id = id;
        this.institutionId = institutionId;
        this.account = account;
        this.status = Status.PENDING;
    }

    public Teacher(InstitutionId institutionId, Account account) {
        this(institutionId, IdCreator.creator(TeacherId.class).create(), account);
    }

    public String getName() {
        return account.getName();
    }
}
