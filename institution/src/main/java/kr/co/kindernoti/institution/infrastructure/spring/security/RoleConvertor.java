package kr.co.kindernoti.institution.infrastructure.spring.security;

import kr.co.kindernoti.institution.domain.model.vo.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class RoleConvertor {
    public static Collection<? extends GrantedAuthority> convert(List<Role> roles) {
        return roles.stream()
                .map(Objects::toString)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
