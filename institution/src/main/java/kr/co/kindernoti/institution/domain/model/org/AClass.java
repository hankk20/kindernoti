package kr.co.kindernoti.institution.domain.model.org;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AClass {

    @EqualsAndHashCode.Include
    private final AClassId id;

    private final GradeId gradeId;

    private final String name;

    private List<ClassTeacher> classTeachers = new ArrayList<>();

    private List<Student> students = new ArrayList<>();

    public AClass(GradeId gradeId, String name) {
        this(gradeId,IdCreator.creator(AClassId.class).create(), name);
    }

    @Builder
    public AClass(GradeId gradeId, AClassId id, String name) {
        Validate.notNull(gradeId);
        Validate.notNull(id);
        Validate.notBlank(name);

        this.id = id;
        this.gradeId = gradeId;
        this.name = name;
    }

    /**
     * 담임교사 조회
     * @return
     */
    public Teacher getMainTeacher() {
        return classTeachers.stream()
                .filter(ClassTeacher::isMain)
                .findFirst()
                .map(ClassTeacher::getTeacher)
                .orElseThrow(() -> new NoDataFoundException("Main Teacher"));
    }

    /**
     * 담임교사 설정
     * @param teacher
     */
    public void setMainTeacher(Teacher teacher) {
        classTeachers.removeIf(ClassTeacher::isMain);
        classTeachers.add(ClassTeacher.of(teacher, ClassTeacher.ClassTeacherType.MAIN));
    }

    /**
     * 부담임 추가
     * @param teacher 교직원
     */
    public void addSubTeacher(Teacher teacher) {
        ClassTeacher classTeacher = ClassTeacher.of(teacher, ClassTeacher.ClassTeacherType.SUB);

        if(classTeachers.contains(classTeacher)) {
            throw new AlreadyDataException("학급에 교사 정보가 이미 존재 합니다. ["+teacher.getName()+"]");
        }
        classTeachers.add(classTeacher);
    }

    /**
     * 이름이 같은지 비교 검사
     * @param aClass
     * @return
     */
    public boolean isSameName(AClass aClass) {

        if(aClass == null) {
            return false;
        }

        if(!this.getGradeId().equals(aClass.getGradeId())){
            return false;
        }

        return this.getName().equals(aClass.getName());
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
