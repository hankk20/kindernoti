package kr.co.kindernoti.auth.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class DefaultAuthorities {

    public static final String DEFAULT_AUTHORITY = "LOGIN_USER";
    public static Collection<? extends GrantedAuthority> get() {
        return List.of(() -> DEFAULT_AUTHORITY);
    }
}
