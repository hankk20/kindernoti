package kr.co.kindernoti.auth.user;

import kr.co.kindernoti.auth.login.ServiceType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 공통 정보
 */
@Getter
@Document("user")
public class User {

    @Id
    private String id;
    private String userId;
    private Set<ServiceType> serviceTypes;

    @Setter
    private String email;

    @Setter
    private Set<String> authorities = new HashSet<>();

    public User(){}

    public User(String userId, String email, Set<ServiceType> serviceTypes) {
        this.userId = userId;
        this.email = email;
        this.serviceTypes = serviceTypes;
        serviceTypes.forEach(types -> authorities.add("ROLE_"+types.name().toUpperCase()));
    }

    public boolean containServiceType(ServiceType serviceType){
        return serviceTypes.contains(serviceType);
    }

    public void addServiceType(ServiceType serviceType) {
        serviceTypes.add(serviceType);
    }

}
