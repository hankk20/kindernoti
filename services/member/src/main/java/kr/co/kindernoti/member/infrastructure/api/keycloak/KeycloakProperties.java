package kr.co.kindernoti.member.infrastructure.api.keycloak;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "keycloak")
public class KeycloakProperties {

    private String url;
    private String realm;
    private String apiUrl;
    private KeycloakAdminProperties admin = new KeycloakAdminProperties();
    private KeycloakClientProperties client = new KeycloakClientProperties();

    @Getter @Setter
    public static class KeycloakAdminProperties {

        public static final String DEFAULT_ADMIN_REALM = "master";
        public static final String DEFAULT_ADMIN_CLIENT_ID = "admin-cli";

        private String username;
        private String password;
        private String realm = DEFAULT_ADMIN_REALM;
        private String clientId = DEFAULT_ADMIN_CLIENT_ID;
        private String clientSecret;

    }

    @Getter @Setter
    public static class KeycloakClientProperties {
        private String clientId;
        private String clientSecret;
    }
}
