package kr.co.kindernoti.institution.interfaces.rest.manage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GradeController {

    /**
     * 학년정보를 조회한다.
     * @param id
     */
    @GetMapping("/organization/{id}/grade")
    public void getGrades(@PathVariable("id") String id) {

    }

    @GetMapping("/organization/{id}/grade/{gradeId}")
    public void getGrade(@PathVariable("id")String id, @PathVariable("gradeId") String gradeId) {

    }
}
