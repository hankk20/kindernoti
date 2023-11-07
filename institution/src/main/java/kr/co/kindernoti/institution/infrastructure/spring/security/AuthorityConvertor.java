package kr.co.kindernoti.institution.infrastructure.spring.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthorityConvertor {
    public static Collection<? extends GrantedAuthority> convert(List<String> authority) {
        if(authority == null) {
            return Collections.emptyList();
        }
        return authority.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
