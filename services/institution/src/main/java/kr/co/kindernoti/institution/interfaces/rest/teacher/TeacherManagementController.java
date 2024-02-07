package kr.co.kindernoti.institution.interfaces.rest.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TeacherManagementController {

    @PutMapping("/manage/institution/{institutionId}/teacher/{teacherId}")
    public void changeStatus(@PathVariable("institutionId")String institutionId
            , @PathVariable("teacherId") String teacherId) {

    }

}
