package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class UserTokenController {

    private final UserTokenService userTokenService;

    @GetMapping("/token")
    public Mono<ResponseEntity<Object>> updateToken(@AuthenticationPrincipal Authentication authentication) {
        return userTokenService.getJwt(authentication.getName())
                .flatMap(token -> Mono.just(ResponseEntity
                        .ok(new JwtService.TokenResponse(token))
                    )
                );
    }
}
