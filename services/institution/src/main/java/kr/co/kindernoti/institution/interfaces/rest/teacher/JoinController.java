package kr.co.kindernoti.institution.interfaces.rest.teacher;

import kr.co.kindernoti.core.spring.security.MemberAuthenticationToken;
import kr.co.kindernoti.institution.application.dto.TeacherDto;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.IdCreator;
import kr.co.kindernoti.institution.interfaces.rest.mapper.TeacherInterfaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class JoinController {

    private final TeacherUseCase teacherUseCase;
    private final TeacherInterfaceMapper teacherInterfaceMapper;

    /**
     * 해당 기관으로 회원가입 처리
     * @param id
     * @param principal
     * @return
     */
    @PostMapping("/institution/{institutionId}/teachers")
    public Mono<ResponseEntity<TeacherDto>> join(@PathVariable("institutionId") String id, @AuthenticationPrincipal MemberAuthenticationToken principal) {
        Account account = Account.of(principal.getId(), principal.getUsername(), principal.getEmail(), null);
        return teacherUseCase.join(IdCreator.creator(InstitutionId.class).from(id), account)
                .map(teacherInterfaceMapper::toDto)
                .map(ResponseEntity::ok);
    }
}
