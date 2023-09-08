package kr.co.kindernoti.auth.configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfiguration {

    /**
     * JWK 개인키
     * parseFormPEMEncodeObjects 메소드를 사용 하기위해 bouncycastle 라이브러리가 필요함
     * @param jwtProperties
     * @return
     * @throws JOSEException
     */
    @Bean
    public RSAKey rsaKey(JwtProperties jwtProperties) throws JOSEException {
        return JWK.parseFromPEMEncodedObjects(jwtProperties.readPrivateKey())
                .toRSAKey();
    }

    /**
     * JWK 공개키
     * @param rsaKey
     * @return
     * @throws JOSEException
     */
    @Bean
    public JWKSet publicRsaKey(RSAKey rsaKey) throws JOSEException {
        RSAKey publicKey = new RSAKey.Builder(rsaKey.toRSAPublicKey())
                .keyUse(KeyUse.SIGNATURE)
                .build();
        return new JWKSet(publicKey.toPublicJWK());
    }
}
