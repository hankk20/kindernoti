package kr.co.kindernoti.institution.domain.model.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {

    private final String street;
    private final String detail;

    public static Address of(String street, String detail) {
        if(StringUtils.isBlank(street)) {
            throw new IllegalArgumentException("Address street is null");
        }
        return new Address(street, detail);
    }
}
