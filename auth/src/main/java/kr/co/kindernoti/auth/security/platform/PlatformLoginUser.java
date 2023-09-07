package kr.co.kindernoti.auth.security.platform;

import com.fasterxml.jackson.annotation.JsonView;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.LoginUser;
import kr.co.kindernoti.auth.security.jwt.JWTView;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Set;

@Getter
public class PlatformLoginUser extends User implements LoginUser<PlatformLoginUser> {

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

    private PlatformLoginUser(PlatformLoginUser source, Set<ServiceType> services, boolean joined) {
        this(source.getUserId(), source.getPassword(),source.getEmail(), services, source.getAuthorities());
    }

    public PlatformLoginUser newUser(ServiceType service) {
        return new PlatformLoginUser(this, Set.of(service), false);
    }

    public PlatformLoginUser joinedUser(ServiceType service){
        return new PlatformLoginUser(this, Set.of(service), true);
    }


}
