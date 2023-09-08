package kr.co.kindernoti.auth.security.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.DefaultAuthorities;
import kr.co.kindernoti.auth.security.jwt.JWTView;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
public class OauthLoginUser implements OidcUser {

    @JsonView(JWTView.class)
    private final String userId;
    @JsonView(JWTView.class)
    private final String email;
    @JsonView(JWTView.class)
    private final OauthProvider oauthProvider;
    @JsonView(JWTView.class)
    private Set<ServiceType> serviceTypes;
    @JsonView(JWTView.class)
    private boolean joined;

    @Builder
    public OauthLoginUser(String userId, String email, OauthProvider oauthProvider) {
        this.userId = userId;
        this.email = email;
        this.oauthProvider = oauthProvider;
    }

    private OauthLoginUser(OauthLoginUser source, Set<ServiceType> serviceType, boolean joined) {
        this(source.getUserId(), source.getEmail(), source.getOauthProvider());
        this.serviceTypes = serviceType;
        this.joined = joined;

    }


    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return DefaultAuthorities.get();
    }

    @JsonIgnore
    @Override
    public String getName() {
        return userId;
    }

}
