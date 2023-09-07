package kr.co.kindernoti.auth.login;

import kr.co.kindernoti.auth.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * ID/PW 로그인 사용자 정보
 */
@Getter
@Document("users")
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
