package kr.co.kindernoti.institution.application.dto;

import kr.co.kindernoti.institution.domain.model.org.AClass;
import kr.co.kindernoti.institution.domain.model.org.AClassId;
import kr.co.kindernoti.institution.domain.model.org.Grade;
import kr.co.kindernoti.institution.domain.model.org.Student;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class AClassDto {

    AClassId id;

    Grade grade;

    String name;

    List<AClass.ClassTeacher> classTeachers;

    List<Student> students;
}
