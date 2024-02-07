package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.YearMonth;

import static org.assertj.core.api.Assertions.*;

class AClassTest {

    Institution institution;
    Grade grade;

    Account account;

    @BeforeEach
    void setUp() {
        institution = TestDataCreator.institutionDomain();
        grade = Grade.builder(institution.getId(), YearMonth.of(2023, 1).toString(), "1학년")
                .build();

    }

    @Test
    @DisplayName("담임 교사 추가")
    void testAddTeacher() {
        Teacher mainTeacher = new Teacher(institution.getId(), Account.of("user", "홍길동", "", null));
        Teacher subTeacher = new Teacher(institution.getId(), Account.of("user", "김길동", "", null));


        AClass aClass = new AClass(grade.getId(), "1반");

        aClass.setMainTeacher(mainTeacher);
        aClass.addSubTeacher(subTeacher);

        assertThat(aClass.getMainTeacher())
                .isEqualTo(mainTeacher);

        assertThat(aClass.getClassTeachers())
                .contains(AClass.ClassTeacher.of(mainTeacher, AClass.ClassTeacher.ClassTeacherType.MAIN))
                .contains(AClass.ClassTeacher.of(subTeacher, AClass.ClassTeacher.ClassTeacherType.SUB));

    }

    @Test
    @DisplayName("담임이 없는 경우 오류")
    void testNoDataMainTeacher() {
        AClass aClass = new AClass(grade.getId(), "1반");
        assertThatThrownBy(aClass::getMainTeacher)
                .isInstanceOf(NoDataFoundException.class);
    }

    @Test
    @DisplayName("이미 추가된 교사 추가 오류")
    void testAlreadySubTeacher() {
        AClass aClass = new AClass(grade.getId(), "1반");
        Teacher subTeacher = new Teacher(institution.getId(), Account.of("user", "김길동", "", null));
        aClass.addSubTeacher(subTeacher);
        assertThatThrownBy(() -> aClass.addSubTeacher(subTeacher))
                .isInstanceOf(AlreadyDataException.class);
    }


}