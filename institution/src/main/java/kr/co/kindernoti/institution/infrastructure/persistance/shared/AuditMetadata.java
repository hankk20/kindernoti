package kr.co.kindernoti.institution.infrastructure.persistance.shared;

import kr.co.kindernoti.institution.domain.model.vo.PlainId;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public abstract class AuditMetadata{

    @Version
    @Setter
    private int version;

    @CreatedBy
    private String createBy;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedBy
    private String modifyBy;

    @LastModifiedDate
    private LocalDateTime modifiedDate;


}
