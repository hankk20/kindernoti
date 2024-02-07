package kr.co.kindernoti.member.infrastructure.spring.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

@Getter
public class UsernamePasswordJwtToken extends AbstractAuthenticationToken {

    private final String accessToken;
    private final String refreshToken;

    public UsernamePasswordJwtToken(String accessToken, String refreshToken) {
        super(Collections.emptyList());
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
