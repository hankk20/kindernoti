package kr.co.kindernoti.auth.user;

import jakarta.validation.constraints.*;
import kr.co.kindernoti.auth.login.ServiceType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter @Setter
public class JoinRequest {

    @NotNull
    @Length(max = 100)
    private String userId;

    @NotBlank
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "[0-9].*")
    private String password;

    @NotNull
    private ServiceType serviceType;

    @Builder
    public JoinRequest(String userId, String email, String password, ServiceType serviceType) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.serviceType = serviceType;
    }

    public PlatformUser toPlatformUser() {
        return PlatformUser.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .service(Set.of(serviceType))
                .build();
    }
}
