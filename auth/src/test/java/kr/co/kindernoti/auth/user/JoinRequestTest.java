package kr.co.kindernoti.auth.user;

import jakarta.validation.ConstraintViolation;
import kr.co.kindernoti.auth.login.ServiceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = LocalValidatorFactoryBean.class)
class JoinRequestTest {

    @Autowired
    LocalValidatorFactoryBean validator;

    @Test
    void testValidate() {
        //given
        JoinRequest build = JoinRequest.builder()
                .userId(sampleData(100))
                .email("eeee@222.com")
                .password("1111")
                .serviceType(ServiceType.parent)
                .build();
        //when
        Set<ConstraintViolation<JoinRequest>> validate = validator.validate(build);
        //then
        assertThat(validate)
                .hasSize(0);
    }

    String sampleData(int length) {
        return IntStream.range(0, length)
                .mapToObj(i -> "a")
                .collect(Collectors.joining());
    }
}