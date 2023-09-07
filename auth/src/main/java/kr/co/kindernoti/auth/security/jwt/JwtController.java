package kr.co.kindernoti.auth.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final RSAKey rsaKey;

    @GetMapping("/.well-known/jwks.json")
    public Mono<Map<String, Object>> wellKnownJwk() throws JOSEException {
        RSAKey build = new RSAKey.Builder(rsaKey.toRSAPublicKey())
                .keyUse(KeyUse.SIGNATURE)
                .build();
        Map<String, Object> jsonObject = new JWKSet(build.toPublicJWK()).toJSONObject();
        return Mono.just(jsonObject);
    }
}
