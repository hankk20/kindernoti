package kr.co.kindernoti.parent.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SampleController {

    @GetMapping("/user")
    public Mono<User> sample(@AuthenticationPrincipal Object o) {
        return Mono.just(new User("한광수", "0101111", UserType.PARENT));
    }
}
