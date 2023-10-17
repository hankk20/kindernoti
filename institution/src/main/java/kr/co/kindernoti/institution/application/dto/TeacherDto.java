package kr.co.kindernoti.institution.application.dto;

import kr.co.kindernoti.institution.domain.model.vo.Status;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TeacherDto {
    private String id;
    private String institutionId;
    private Account account;
    private Status status;
}
