package kr.co.kindernoti.auth.user.specification;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.kindernoti.auth.login.OauthProvider;
import kr.co.kindernoti.auth.login.QOauthUser;
import lombok.Builder;

import java.util.Objects;
import java.util.stream.Stream;

public class OauthUserSpecification {

    private final QOauthUser oauthUser = QOauthUser.oauthUser;
    private String userId;
    private OauthProvider oauthProvider;

    @Builder
    public OauthUserSpecification(String userId, OauthProvider oauthProvider) {
        this.userId = userId;
        this.oauthProvider = oauthProvider;
    }

    private BooleanExpression userIdEquals() {
        if(userId == null) {
            return null;
        }
        return oauthUser.userId.eq(userId);
    }

    private BooleanExpression oauthProviderEquals() {
        if(oauthProvider == null) {
            return null;
        }
        return oauthUser.oauthProvider.eq(oauthProvider);
    }

    public Predicate toPredicate() {
        return Stream.of(userIdEquals(), oauthProviderEquals())
                .filter(Objects::nonNull)
                .reduce((a ,b) -> a.and(b))
                .orElse(null);
    }


}
