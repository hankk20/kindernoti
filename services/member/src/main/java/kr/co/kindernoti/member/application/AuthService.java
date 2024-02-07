package kr.co.kindernoti.member.application;

import kr.co.kindernoti.member.application.in.AuthUseCase;
import kr.co.kindernoti.member.application.out.AuthPort;
import kr.co.kindernoti.member.application.out.MemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthService implements AuthUseCase {

    private final AuthPort authPort;
    private final MemberPort memberPort;

    @Override
    public Mono<Void> addMemberRole(String userId, String roleName) {
        return authPort.getRole(roleName)
                .flatMap(role -> memberPort.addMemberRole(userId, role))
                .then();
    }
}
