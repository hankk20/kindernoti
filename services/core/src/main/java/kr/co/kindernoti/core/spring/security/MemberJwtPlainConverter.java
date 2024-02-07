package kr.co.kindernoti.core.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.representations.AccessToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class MemberJwtPlainConverter implements Converter<String, MemberAuthenticationToken> {

    @Override
    public MemberAuthenticationToken convert(String source) {
        try {
            AccessToken accessToken = new JWSInput(source).readJsonContent(AccessToken.class);
            List<SimpleGrantedAuthority> list = accessToken.getRealmAccess().getRoles().stream()
                    .map(s -> "ROLE_"+s)
                    .map(SimpleGrantedAuthority::new).toList();
            return new MemberAuthenticationToken(accessToken.getSubject(), accessToken.getPreferredUsername(), accessToken.getEmail(), list);
        } catch (Exception e) {
            log.error("[member] jwt convert error", e);
            throw new RuntimeException(e);
        }
    }
}
