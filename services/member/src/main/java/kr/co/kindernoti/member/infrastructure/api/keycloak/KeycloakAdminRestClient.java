package kr.co.kindernoti.member.infrastructure.api.keycloak;

import kr.co.kindernoti.member.application.dto.RoleInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
@Component
public class KeycloakAdminRestClient {

    private final WebClient webClient;
    private final KeycloakProperties keycloakProperties;
    private static final String CONTEXT_TOKEN_NAME = "ADMIN_ACCESS_TOKEN";

    /**
     * AccessToken을 발급 받는다.
     *
     * @return
     */
    public Mono<AccessToken> clientAccessToken() {
        return accessToken(keycloakProperties.getAdmin().getRealm(), createClientSecret());
    }

    public Mono<AccessToken> accessToken(String realm, BodyInserters.FormInserter<String> body) {
        return webClient.mutate()
                .baseUrl(keycloakProperties.getUrl())
                .build()
                .post()
                .uri("/realms/{realm}/protocol/openid-connect/token", realm)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve().bodyToMono(AccessToken.class);
    }

    private Mono<WebClient> adminClient() {
        return clientAccessToken().cache(Duration.ofMinutes(5))
                .map(token -> webClient.mutate()
                        .baseUrl(keycloakProperties.getApiUrl())
                        .defaultHeaders(header -> header.setBearerAuth(token.getAccessToken()))
                        .build());
    }

    public Mono<String> getClientId(String clientName) {
        return adminClient()
                .flatMap(adminClient -> adminClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/clients")
                                .queryParam("clientId", keycloakProperties.getClient().getClientId())
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<ClientRepresentation>>() {})
                        .doOnNext(r -> log.info("[keycloak api] Client Info {}", r))
                        .map(list -> list.stream()
                                .filter(client -> client.getClientId().equals(clientName)).findFirst()
                                .orElseThrow(NoSuchElementException::new)
                                .getId())
                );
    }

    /**
     * id로 사용자 정보를 조회 한다.
     *
     * @param id
     * @return
     */
    public Mono<UserRepresentation> getUser(String id) {
        return adminClient()
                .flatMap(adminClient -> adminClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/users/{id}")
                                .build(id))
                        .retrieve()
                        .bodyToMono(UserRepresentation.class));
    }

    /**
     * 사용자 정보를 수정 한다.
     *
     * @param user
     * @return
     */
    public Mono<Void> updateUser(UserRepresentation user) {
        return adminClient()
                .flatMap(adminClient -> adminClient.put()
                        .uri(uriBuilder -> uriBuilder.path("/users/{id}")
                                .build(user.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    /**
     * username으로 사용자를 검색한다.
     *
     * @param username
     * @return
     */
    public Mono<UserRepresentation> findUser(String username) {
        return adminClient()
                .flatMap(adminClient -> adminClient.get()
                        .uri(builder -> builder.path("/users")
                                .queryParam("username", username)
                                .build(keycloakProperties.getRealm()))
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<UserRepresentation>>() {})
                        .map(l -> l.stream().filter(user -> user.getUsername().equals(username))
                                .findFirst()
                                .orElseThrow(NoSuchElementException::new)
                        ));
    }

    /**
     * 사용자를 생성하고 username으로 아이디를 조회하여 리턴한다.
     *
     * @param user
     * @return
     */
    public Mono<Void> createUser(UserRepresentation user) {
        return adminClient()
                .flatMap(adminClient -> adminClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/users").build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user)
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    /**
     * Realm Role 정보를 조회 한다.
     * @param roleName
     * @return
     */
    public Mono<RoleInfo> getRealmRole(String roleName) {
        return adminClient()
                .flatMap(adminClient -> adminClient.get()
                        .uri(uriBuilder -> uriBuilder.path("/roles")
                                .queryParam("search", roleName)
                                .build())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<RoleRepresentation>>() {})
                        .map(roles -> roles.stream()
                                        .filter(role -> role.getName().equals(roleName))
                                        .findFirst()
                                        .map(role -> new RoleInfo(role.getId(), role.getName()))
                                        .orElseThrow(NoSuchElementException::new)));
    }

    /**
     * User에 Realm Role을 지정한다.
     * @param userId
     * @param roleInfo
     * @return
     */
    public Mono<Void> addUserRealmRole(String userId, RoleInfo roleInfo) {
        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(roleInfo.id());
        roleRepresentation.setName(roleInfo.name());
        return adminClient()
                .flatMap(adminClient -> adminClient.post()
                        .uri(uriBuilder -> uriBuilder.path("/users/{userId}/role-mappings/realm")
                                .build(userId))
                        .bodyValue(List.of(roleRepresentation))
                        .retrieve()
                        .bodyToMono(Void.class));
    }

    /**
     * Client Secret 이 존재 하면 Client ID로 Token요청 Body를 생성하고
     * 존재 하지 않으면 Admin 계정으로 Token요청 Body를 생성한다.
     *
     * @return Token 요청 Form Body
     */
    private BodyInserters.FormInserter<String> createClientSecret() {
        KeycloakProperties.KeycloakAdminProperties admin = keycloakProperties.getAdmin();
        if (Objects.nonNull(admin.getClientSecret())) {
            return BodyInserters.fromFormData("client_id", admin.getClientId())
                    .with("client_secret", admin.getClientSecret())
                    .with("grant_type", "client_credentials");
        }
        return BodyInserters.fromFormData("client_id", admin.getClientId())
                .with("username", admin.getUsername())
                .with("password", admin.getPassword())
                .with("grant_type", "password");
    }

    public BodyInserters.FormInserter<String> createRequestTokenBody(String username, String password) {
        KeycloakProperties.KeycloakClientProperties client = keycloakProperties.getClient();
        return BodyInserters.fromFormData("client_id", client.getClientId())
                .with("client_secret", client.getClientSecret())
                .with("username", username)
                .with("password", password)
                .with("grant_type", "password");
    }


}
