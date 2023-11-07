package kr.co.kindernoti.institution.domain.model.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;


@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class Phone {

    private final String number;

    private final PhoneType phoneType;

    public static Phone of(String number, PhoneType phoneType) {

        String noWhitespace = StringUtils.deleteWhitespace(number);

        if(phoneType == null) {
            throw new IllegalArgumentException("PhoneType is null");
        }
        if(noWhitespace == null) {
            throw new IllegalArgumentException("Phone Number is null");
        }

        String onlyNumber = noWhitespace.replaceAll("-", "");

        if(!phoneType.pattern.asPredicate().test(onlyNumber)) {
            throw new IllegalArgumentException("전화번호 형식이 잘못 되었습니다. ["+number+"]");
        }

        return new Phone(number, phoneType);
    }

    @Getter
    public enum PhoneType {
        TEL("^\\d{9,11}$"),
        MOBILE("^01\\d{8,9}$");

        private final Pattern pattern;

        PhoneType(String pattern) {
            this.pattern = Pattern.compile(pattern);;
        }
    }
}
