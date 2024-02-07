package kr.co.kindernoti.core.spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.co.kindernoti.core.spring.security.role.ClientRole;
import kr.co.kindernoti.core.spring.security.role.RealmRole;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.representations.AccessToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class MemberJwtConverter implements Converter<Jwt, Mono<MemberAuthenticationToken>> {

    private final MemberJwtPlainConverter memberJwtPlainConverter = new MemberJwtPlainConverter();

    @Override
    public Mono<MemberAuthenticationToken> convert(Jwt source) {
        return Mono.just(Objects.requireNonNull(memberJwtPlainConverter.convert(source.getTokenValue())));
    }

}
