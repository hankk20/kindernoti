package kr.co.kindernoti.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherInfo {
    private String institutionId;
    private String gradeId;
    private String classId;
}
