package kr.co.kindernoti.member.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberJoinDto {
    String username;
    String password;
    String fullName;
    String email ;
}
