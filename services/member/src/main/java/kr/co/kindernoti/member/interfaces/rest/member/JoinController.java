package kr.co.kindernoti.member.interfaces.rest.member;

import kr.co.kindernoti.member.application.in.JoinUseCase;
import kr.co.kindernoti.member.infrastructure.api.keycloak.AccessToken;
import kr.co.kindernoti.member.interfaces.rest.JoinMapper;
import kr.co.kindernoti.member.interfaces.rest.dto.MemberCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class JoinController {

    private final JoinUseCase joinUseCase;
    private final JoinMapper joinMapper;

    /**
     * Form 회원가입
     *
     * @param joinCommand
     * @return
     */
    @PostMapping("/join")
    public Mono<ResponseEntity<?>> join(@RequestBody @Validated MemberCommand.JoinCommand joinCommand) {
        return joinUseCase.join(joinMapper.toDto(joinCommand))
                .then(Mono.just(ResponseEntity.ok().build()));
    }


}
