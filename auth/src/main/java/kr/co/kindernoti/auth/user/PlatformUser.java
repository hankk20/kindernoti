package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.login.ServiceType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * ID/PW 로그인 사용자 정보
 */
@Getter
@Document("user")
@TypeAlias("platformUser")
public class PlatformUser extends User {

    private String password;

    private PlatformUser() {
        super();
    }

    @Builder
    public PlatformUser(String userId, String password, String email, Set<ServiceType> service) {
        super(userId, email, service);
        this.password = password;
    }

    public void changePassword(String password) {
        this.password = password;
    }

}
