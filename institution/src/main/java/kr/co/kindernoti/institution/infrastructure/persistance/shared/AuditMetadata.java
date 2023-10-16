package kr.co.kindernoti.institution.infrastructure.persistance.shared;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public class AuditMetadata {

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String modifyBy;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
