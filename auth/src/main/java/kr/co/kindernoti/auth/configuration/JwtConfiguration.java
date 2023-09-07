package kr.co.kindernoti.auth.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    @Bean
    public RSAKey rsaKey(JwtProperties jwtProperties) throws JOSEException {
        return JWK.parseFromPEMEncodedObjects(jwtProperties.readPrivateKey())
                .toRSAKey();
    }
}
