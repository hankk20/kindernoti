package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.*;

class GradeTest {

    private Institution institution;
    private Grade grade;

    @BeforeEach
    void setUp() {
        institution = TestDataCreator.institutionDomain();
        grade = Grade.builder(institution.getId(), YearMonth.of(2023, 1).toString(), "1학년")
                .build();
    }

    @Test
    @DisplayName("학년 반 정보 생성")
    void test() {
        //when
        AClass aClass = grade.createAClass("1반");

        //then
        assertThat(grade.getName())
                .isEqualTo("1학년");
        assertThat(aClass.getGradeId())
                .isEqualTo(grade.getId());
    }

    @Test
    @DisplayName("동일한 반이름 추가 오류")
    void testAddAClass() {
        grade.createAClass("1반");
        assertThatThrownBy(() -> grade.createAClass("1반"))
                .isInstanceOf(AlreadyDataException.class)
                .hasMessageContaining("1반");
    }





}