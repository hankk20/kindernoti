package kr.co.kindernoti.auth.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JoinController {

    private final UserService userService;

    @PostMapping("/join")
    public Mono<ResponseEntity<Object>> join(@RequestBody @Validated Mono<JoinRequest> request){
        return request.map(JoinRequest::toPlatformUser)
                .flatMap(userService::join)
                .map(u -> ResponseEntity.ok().build());

    }


}
