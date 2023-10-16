package kr.co.kindernoti.institution.domain.model.vo;

import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class Account {

    String userId;
    String name;
    String email;
    Phone phone;
    List<Role> roles;

}
