package kr.co.kindernoti.institution.infrastructure.persistance.org.model;

import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.org.AClassId;
import kr.co.kindernoti.institution.domain.model.org.Student;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.apache.commons.lang3.Validate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document
public class AClassData {

    @EqualsAndHashCode.Include
    @Id
    private final AClassId id;

    private final String name;

    private List<ClassTeacher> classTeachers = new ArrayList<>();

    private List<Student> students = new ArrayList<>();

    public AClassData(String name) {
        Validate.notBlank(name);
        this.id = IdCreator.creator(AClassId.class).create();
        this.name = name;
    }

    @Value(staticConstructor = "of")
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class ClassTeacher {

        @EqualsAndHashCode.Include
        Teacher teacher;

        ClassTeacherType classTeacherType;

        public boolean isMain() {
            return classTeacherType == ClassTeacherType.MAIN;
        }

        public enum ClassTeacherType {
            MAIN,
            SUB
        }
    }

}
