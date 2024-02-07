package kr.co.kindernoti.auth.security.jwt;

import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JWKSet jwkSet;

    @GetMapping("/.well-known/jwks.json")
    public Mono<ResponseEntity<Map<String,Object>>> wellKnownJwk() {
        return Mono.just(ResponseEntity.ok(jwkSet.toJSONObject()));
    }
}
