package kr.co.kindernoti.core.spring.security.role;

import org.springframework.security.core.GrantedAuthority;

public class MemberAuthority implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return null;
    }
}
