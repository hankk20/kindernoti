package kr.co.kindernoti.institution.interfaces.rest.teacher;

import kr.co.kindernoti.core.spring.security.MemberAuthenticationToken;
import kr.co.kindernoti.institution.application.dto.TeacherDto;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.interfaces.rest.mapper.TeacherInterfaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 교직원
 */
@RequiredArgsConstructor
@RestController
public class TeacherController {

    private final TeacherUseCase teacherUseCase;

    private final TeacherInterfaceMapper teacherInterfaceMapper;

    @GetMapping("/teachers/{id}")
    public Mono<ResponseEntity<TeacherDto>> findUser(@PathVariable String id) {
        return teacherUseCase.findById(IdCreator.creator(TeacherId.class).from(id))
                .map(teacherInterfaceMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/me")
    public Flux<TeacherDto> myInfo(@AuthenticationPrincipal MemberAuthenticationToken account) {
        return teacherUseCase.findTeacherByUserId(account.getId())
                .map(teacherInterfaceMapper::toDto);
    }


}
