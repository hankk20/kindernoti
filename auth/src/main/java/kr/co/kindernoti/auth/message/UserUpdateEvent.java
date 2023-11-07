package kr.co.kindernoti.auth.message;

import lombok.*;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserUpdateEvent {
    String userId;
    String email;
    Set<String> authorities;
}
