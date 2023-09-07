package kr.co.kindernoti.auth.user.specification;

import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OauthUserSpecificationTest {

    @Test
    void testToPredicate() {
        Predicate predicate = OauthUserSpecification
                .builder()
                .userId("sss")
                .build()
                .toPredicate();
        assertThat(predicate)
                .isNotNull();
    }
}