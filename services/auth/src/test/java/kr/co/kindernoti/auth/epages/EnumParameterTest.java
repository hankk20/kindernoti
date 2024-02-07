package kr.co.kindernoti.auth.epages;

import kr.co.kindernoti.auth.login.OauthProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumParameterTest {

    @Test
    @DisplayName("EnumParameterDescripter enumValues Attribute 확인")
    void test() {

        //given
        EnumParameter enumParameter = EnumParameter.of(OauthProvider.class);

        //when
        ParameterDescriptor provider = enumParameter.withName("provider");

        //then
        assertResult(provider.getAttributes());
    }

    @Test
    @DisplayName("Enum Type이 아닌 객체로 생성 오류 확인")
    void testError(){
        assertThatThrownBy(() -> {
            EnumParameter.of(String.class);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("String List로 생성")
    void testStringEnum() {
        //given when
        ParameterDescriptor parameterDescriptor = EnumParameter.of(List.of("kakao", "google", "naver"))
                .withName("provider");

        //then
        assertResult(parameterDescriptor.getAttributes());
    }

    void assertResult(Map<String, Object> map){
        assertThat(map)
                .hasEntrySatisfying(EnumParameter.ENUM_VALUES_KEY
                        , o -> {
                            assertThat(o)
                                    .isInstanceOf(List.class);
                            assertThat((List<String>) o)
                                    .contains(OauthProvider.class.getEnumConstants()[0].toString());
                        });
    }

}