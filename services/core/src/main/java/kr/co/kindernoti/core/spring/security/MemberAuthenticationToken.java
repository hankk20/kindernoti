package kr.co.kindernoti.core.spring.security;

import kr.co.kindernoti.core.spring.security.role.ClientRole;
import kr.co.kindernoti.core.spring.security.role.RealmRole;
import kr.co.kindernoti.core.spring.security.role.Role;
import lombok.Getter;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.authorization.Permission;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Getter
public class MemberAuthenticationToken extends AbstractAuthenticationToken {

    private final String id;
    private final String username;
    private final String email;

    public MemberAuthenticationToken(String id, String username, String email, List<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.id = id;
        this.username = username;
        this.email = email;
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
