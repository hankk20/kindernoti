package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.domain.model.vo.Account;
import lombok.Value;

@Value(staticConstructor = "of")
public class AccountUpdateEvent {
    Account account;
}
