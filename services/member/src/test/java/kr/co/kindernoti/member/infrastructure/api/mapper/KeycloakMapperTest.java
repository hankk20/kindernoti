package kr.co.kindernoti.member.infrastructure.api.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kindernoti.member.domain.TeacherInfo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class KeycloakMapperTest {

    @Test
    void test() {
        KeycloakMapper mapper = Mappers.getMapper(KeycloakMapper.class);
        TeacherInfo teacherInfo = new TeacherInfo("2", "2", null);
        TeacherInfo teacherInfo1 = new TeacherInfo("1", null, null);
        TeacherInfo teacherInfo2 = mapper.patchTeacherInfo(teacherInfo1, teacherInfo);
        assertThat(teacherInfo2)
                .extracting(TeacherInfo::getInstitutionId)
                .isNotNull();
    }

}