package kr.co.kindernoti.auth.security.platform;

import com.fasterxml.jackson.annotation.JsonView;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.jwt.JWTView;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

@Getter
public class PlatformLoginUser extends User{

    @JsonView(JWTView.class)
    private String userId;
    @JsonView(JWTView.class)
    private Set<ServiceType> services;
    @JsonView(JWTView.class)
    private String email;
    @Builder(builderMethodName = "customBuilder")
    public PlatformLoginUser(String userId, String password, String email, Set<ServiceType> services, Collection<? extends GrantedAuthority> authorities) {
        super(userId, password, authorities);
        this.userId = userId;
        this.services = services;
        this.email = email;
    }

}
