package kr.co.kindernoti.member.interfaces.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

public class MemberCommand {

    /**
     * 기본회원가입
     */
    public record JoinCommand(@Length(min = 1, max = 20)
                              @NotBlank
                              String username
                            , @NotBlank
                              String password
                            , @Length(min = 1, max = 20)
                              @NotBlank
                              String fullName
                            , @Email
                              String email) {}


}
