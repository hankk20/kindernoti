package kr.co.kindernoti.auth.epages;

import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.request.RequestDocumentation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * PathParameter나 QueryParameter는 {@link com.epages.restdocs.apispec.EnumFields}를 사용 할 수 없어서 만듬
 * <pre>
 * example:
 * {@code EnumParameter.of(OauthProvider.class)
 *      .withName("provider")}
 * </pre>
 */
public class EnumParameter {

    private final List<String> enums;
    public static final String ENUM_VALUES_KEY = "enumValues";

    private EnumParameter(List<String> enums) {
        this.enums = enums;
    }

    /**
     * enum으로 만들 String 목록을 받아서 EnumParameter 객체를 생성한다.
     * @param list
     * @return
     */
    public static EnumParameter of(List<String> list) {
        return new EnumParameter(list);

    }

    /**
     * Enum Class를 받아서 enum 정보를 String List로 변환한다.
     * @param clazz
     * @return
     */
    public static EnumParameter of(Class<?> clazz) {
        if(clazz.isEnum()) {
            List<String> collect = Arrays.stream(clazz.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.toList());
            return new EnumParameter(collect);
        }
        throw new IllegalArgumentException("["+clazz+"] is not Enum ");
    }

    /**
     * enums를 Attribute로 저장하고 ParameterDescriptor를 반환한다.
     * @param name
     * @return
     */
    public ParameterDescriptor withName(String name) {
        return RequestDocumentation.parameterWithName(name)
                .attributes(key(ENUM_VALUES_KEY).value(enums));
    }

}
