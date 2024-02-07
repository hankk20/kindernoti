package kr.co.kindernoti.institution.domain.model.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class Account {

    private final String userId;
    private String name;
    private String email;
    private Phone phone;

    private Account(String userId, String name, String email, Phone phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public static Account of(String userId, String name, String email, Phone phone) {
        return new Account(userId, name, email, phone);
    }

}
