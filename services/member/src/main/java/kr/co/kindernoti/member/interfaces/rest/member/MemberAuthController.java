package kr.co.kindernoti.member.interfaces.rest.member;

import kr.co.kindernoti.core.spring.security.MemberAuthenticationToken;
import kr.co.kindernoti.member.application.in.AuthUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class MemberAuthController {

    private final AuthUseCase authUseCase;

    @PutMapping("/member/roles")
    public Mono<ResponseEntity<?>> addMemberRole(@AuthenticationPrincipal MemberAuthenticationToken principal) {
        return authUseCase.addMemberRole(principal.getId(), "teacher")
                .then(Mono.just(ResponseEntity.ok().build()));
    }
}
