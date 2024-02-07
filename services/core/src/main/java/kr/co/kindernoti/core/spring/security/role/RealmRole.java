package kr.co.kindernoti.core.spring.security.role;

import lombok.Getter;

import java.util.List;

@Getter
public class RealmRole implements Role{

    private static final String rolePrefix = "realm_";

    public List<String> roles;

    private RealmRole(List<String> roles) {
        this.roles = roles;
    }

    public static RealmRole create(List<String> roles) {
        return new RealmRole(roles.stream().map(s -> rolePrefix+s)
                .toList());
    }
}
