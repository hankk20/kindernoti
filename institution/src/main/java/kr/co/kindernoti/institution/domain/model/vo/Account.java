package kr.co.kindernoti.institution.domain.model.vo;

import lombok.*;
import lombok.experimental.NonFinal;

import java.util.List;

@Getter
@Setter
public class Account {

    private final String userId;
    private String name;
    private String email;
    private Phone phone;
    private List<String> authorities;

    private Account(String userId, String name, String email, Phone phone, List<String> authorities) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.authorities = authorities;
    }

    public static Account of(String userId, String name, String email, Phone phone, List<String> authorities) {
        return new Account(userId, name, email, phone, authorities);
    }
}
