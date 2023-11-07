package kr.co.kindernoti.institution.infrastructure.spring.security;

import kr.co.kindernoti.institution.domain.model.vo.Account;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class HeaderAuthenticationToken extends AbstractAuthenticationToken {

    private final Account account;

    public HeaderAuthenticationToken(Account account) {
        super(AuthorityConvertor.convert(account.getAuthorities()));
        this.account = account;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return account.getUserId();
    }

    @Override
    public Object getPrincipal() {
        return account;
    }
}
