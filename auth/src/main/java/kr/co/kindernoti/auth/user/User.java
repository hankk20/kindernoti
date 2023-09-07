package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.login.ServiceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Set;

/**
 * 사용자 공통 정보
 */
@Getter
public abstract class User {

    @Id
    private String id;
    private String userId;

    @Setter
    private String email;
    private Set<ServiceType> serviceTypes;

    protected User(){}

    protected User(String userId, String email, Set<ServiceType> serviceTypes) {
        this.userId = userId;
        this.email = email;
        this.serviceTypes = serviceTypes;
    }

    public boolean containServiceType(ServiceType serviceType){
        return serviceTypes.contains(serviceType);
    }

    public void addServiceType(ServiceType serviceType) {
        serviceTypes.add(serviceType);
    }
}
