package kr.co.kindernoti.institution.infrastructure.basedata;

import kr.co.kindernoti.institution.infrastructure.persistance.shared.AuditMetadata;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Document("baseDataFile")
@TypeAlias("baseDataFile")
public class BaseDataFile extends AuditMetadata{

    @Id
    private String id;

    private final String filename;

    private final LocalDateTime lastFileModified;

    @Setter
    private BaseDataFileStatus status;

    @Setter
    private String failMessage;

    public BaseDataFile(String filename, LocalDateTime lastFileModified) {
        this.filename = filename;
        this.lastFileModified = lastFileModified;
    }

    public static BaseDataFile from(File file) {
        Instant instant = Instant.ofEpochMilli(file.lastModified());
        return new BaseDataFile(file.getName(), instant.atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    public enum BaseDataFileStatus {
        SUCCESS,
        FAIL
    }
}
