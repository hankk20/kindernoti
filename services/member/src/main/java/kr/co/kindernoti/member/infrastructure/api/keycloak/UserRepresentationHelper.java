package kr.co.kindernoti.member.infrastructure.api.keycloak;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.beanutils.BeanUtils;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Map;
import java.util.Objects;

public class UserRepresentationHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepresentation userRepresentation;

    private UserRepresentationHelper(UserRepresentation userRepresentation) {
        this.userRepresentation = userRepresentation;
    }

    public static UserRepresentationHelper of(UserRepresentation userRepresentation) {
        return new UserRepresentationHelper(userRepresentation);
    }

    public void addAttribute(Object o) {
        setAttribute(o);
    }

    public void addJsonAttribute(String name, Object o) {
        try {
            userRepresentation.singleAttribute(name, objectMapper.writeValueAsString(o));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getAttribute(String name, Class<T> clazz) {
        String s = userRepresentation.firstAttribute(name);
        if(s == null) {
            return null;
        }
        try {
            return objectMapper.readValue(s, clazz);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void setAttribute(Object o) {
        try {
            Map<String, String> describe = BeanUtils.describe(o);
            describe.keySet().stream()
                    .filter(k -> Objects.nonNull(describe.get(k)))
                    .forEach(key -> userRepresentation.singleAttribute(key, describe.get(key)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserRepresentation getUserRepresentation() {
        return userRepresentation;
    }
}
