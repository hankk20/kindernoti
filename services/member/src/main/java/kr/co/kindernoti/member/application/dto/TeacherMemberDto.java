package kr.co.kindernoti.member.application.dto;

import kr.co.kindernoti.member.domain.TeacherInfo;
import lombok.Value;

/**
 * 교직원 기본 정보
 */
@Value(staticConstructor = "of")
public class TeacherMemberDto {
    String id;
    TeacherInfo teacherInfo;
}
