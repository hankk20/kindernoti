package kr.co.kindernoti.auth.login;

import kr.co.kindernoti.auth.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * Oauth 로그인 사용자 정보
 */
@Getter
@Document("users")
@TypeAlias("oauthUser")
public class OauthUser extends User {

    private OauthProvider oauthProvider;

    private OauthUser(){
        super();
    }

    @Builder
    public OauthUser(String userId, OauthProvider oauthProvider, String email, Set<ServiceType> serviceTypes) {
        super(userId, email, serviceTypes);
        this.oauthProvider = oauthProvider;
    }

}
