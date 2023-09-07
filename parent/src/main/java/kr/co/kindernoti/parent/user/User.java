package kr.co.kindernoti.parent.user;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@TypeAlias("user")
@Getter
public class User {
    @Id
    private String id;
    private String name;
    private String phoneNumber;
    private UserType userType;

    public User(String name, String phoneNumber, UserType userType) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
    }
}
