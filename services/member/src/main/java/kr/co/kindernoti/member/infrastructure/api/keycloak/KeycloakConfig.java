package kr.co.kindernoti.member.infrastructure.api.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
public class KeycloakConfig {

    @Bean
    public WebClient keycloakWebClient(KeycloakProperties keycloakProperties) {
        return WebClient.builder()
                .filter(logRequest())
                .defaultHeaders(h -> h.setAccept(List.of(MediaType.APPLICATION_JSON)))
                .defaultStatusHandler(HttpStatusCode::isError
                        , response ->
                            response.createException()
                                    .doOnNext(e -> log.error("[Keycloak API ERROR] Status : {}, Body : {}", response.statusCode(), e.getResponseBodyAsString()))

                        )
                .build();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            log.info("Request Body : {}", clientRequest.body());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }


    @Bean
    public RealmResource realmResource(KeycloakProperties properties) {
        KeycloakProperties.KeycloakAdminProperties keycloakAdminProperties = properties.getAdmin();
        return KeycloakBuilder.builder()
                .serverUrl(properties.getUrl())
                .realm(keycloakAdminProperties.getRealm())
                .clientId("restapi")
                .clientSecret("tF2x0fDu5YKYzmUTJ4OnodKjfMR8YFHX")
                .grantType("client_credentials")
//                .clientId(keycloakAdminProperties.getClientId())
//                .username(keycloakAdminProperties.getUsername())
//                .password(keycloakAdminProperties.getPassword())
                .build()
                .realm(properties.getRealm());
    }


}
