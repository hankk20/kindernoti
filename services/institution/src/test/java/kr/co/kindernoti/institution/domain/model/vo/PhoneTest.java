package kr.co.kindernoti.institution.domain.model.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PhoneTest {

    @Test
    @DisplayName("전화번호 생성")
    void test() {
        //given
        String p1 = "021112222";
        String p2 = "0211112222";
        String p3 = "03111112222";

        //then
        thenPone(p1, Phone.PhoneType.TEL);
        thenPone(p2, Phone.PhoneType.TEL);
        thenPone(p3, Phone.PhoneType.TEL);
    }

    @Test
    @DisplayName("휴대폰번호 생성")
    void testMobile() {
        //given
        String p1 = "01011112222";
        //then
        thenPone(p1, Phone.PhoneType.MOBILE);
    }

    @Test
    @DisplayName("전화번호 유효성 오류")
    void testTelThrowException() {
        //given
        String shotNumber = "02";
        String longNumber = "033111112222";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of(shotNumber, Phone.PhoneType.TEL));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of(longNumber, Phone.PhoneType.TEL));
    }

    @Test
    @DisplayName("휴대폰번호 유효성 오류")
    void testMobileThrowException() {
        //given
        String errorNumber = "02011112222";
        String shotNumber = "010113333";

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of(errorNumber, Phone.PhoneType.MOBILE));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of(shotNumber, Phone.PhoneType.MOBILE));
    }

    @Test
    @DisplayName("전화번호 동등성 테스트")
    void testEquals() {
        String number = "0211113333";
        Phone phone = Phone.of(number, Phone.PhoneType.TEL);
        Phone phone1 = Phone.of(number, Phone.PhoneType.TEL);
        assertThat(phone)
                .isEqualTo(phone1);
    }

    @Test
    @DisplayName("필수값 테스트")
    void testRequireProperty(){
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of(null, Phone.PhoneType.TEL))
                .withMessageContaining("is null");

        assertThatIllegalArgumentException()
                .isThrownBy(() -> Phone.of("1", null))
                .withMessageContaining("is null")
        ;
    }

    private void thenPone(String number, Phone.PhoneType phoneType) {
        Phone phone = Phone.of(number, phoneType);
        assertThat(phone)
                .isNotNull()
                .extracting(Phone::getNumber)
                .isEqualTo(number);
    }

}