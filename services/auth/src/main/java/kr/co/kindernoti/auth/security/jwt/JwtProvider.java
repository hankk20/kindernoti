package kr.co.kindernoti.auth.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import kr.co.kindernoti.auth.configuration.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final RSAKey rsaKey;
    private final JwtProperties jwtProperties;
    private final Jackson2ObjectMapperBuilder jsonBuilder;
    private ObjectMapper objectMapper;

    /**
     * JwtProvider에서만 필요한 설정을 적용하기 위햄 jsonBuilder를 사용함
     */
    @PostConstruct
    public void post() {
        //JsonView Annotation이 없는 속성은 직렬화 하지 않도록 설정함
        objectMapper = jsonBuilder.featuresToDisable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .build();
    }

    public String create(Object obj) {
        Duration expire = jwtProperties.getExpire();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireDateTime = now.plusSeconds(expire.toSeconds());
        JWTClaimsSet claims = null;

        try {
            claims = JWTClaimsSet.parse(objectMapper.writerWithView(JWTView.class).writeValueAsString(obj));
        } catch (ParseException | JsonProcessingException e) {
            log.error("LoginUser Json 변환 오류", e);
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            problemDetail.setDetail("서버 오류가 발생 하였습니다.");
            problemDetail.setTitle("서버 오류가 발생 하였습니다.");
            throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, problemDetail, e);
        }
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder(claims)
                .issuer(jwtProperties.getIssuer())
                .issueTime(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expirationTime(Date.from(expireDateTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256)
                        .type(JOSEObjectType.JWT)
                        .build(),
                claimsSet);

        try {
            signedJWT.sign(new RSASSASigner(rsaKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

        return signedJWT.serialize();
    }

    public boolean verify(String s){
        try {
            SignedJWT parse = SignedJWT.parse(s);
            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            return parse.verify(verifier);
        } catch (ParseException e) {
            throw new BadCredentialsException("인증정보가 유효하지 않습니다.");
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}

