package kr.co.kindernoti.member.infrastructure.api.keycloak;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Container;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(MockitoExtension.class)
class KeycloakAdminRestClientTest {

    @Container
    private static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:23.0.4").withRealmImportFile("/realm-export.json");

    private KeycloakAdminRestClient client;

    @BeforeAll
    static void beforeAll() {
        keycloak.start();
    }

    @BeforeEach
    void beforeEach() {
        KeycloakProperties keycloakProperties = new KeycloakProperties();
        keycloakProperties.setUrl(keycloak.getAuthServerUrl());
        keycloakProperties.setRealm("testcontainer");
        keycloakProperties.setApiUrl(keycloak.getAuthServerUrl()+"/admin/realms/testcontainer");

        KeycloakProperties.KeycloakAdminProperties keycloakAdminProperties = new KeycloakProperties.KeycloakAdminProperties();
        keycloakAdminProperties.setClientId("admin-cli");
        keycloakAdminProperties.setUsername(keycloak.getAdminUsername());
        keycloakAdminProperties.setPassword(keycloak.getAdminPassword());
        keycloakProperties.setAdmin(keycloakAdminProperties);

        KeycloakProperties.KeycloakClientProperties keycloakClientProperties = new KeycloakProperties.KeycloakClientProperties();
        keycloakClientProperties.setClientId("testcontainer-client");
        keycloakClientProperties.setClientSecret("SmEJvKDdCT2WC4YlgI2iFeHyqRId1o2Q");
        keycloakProperties.setClient(keycloakClientProperties);

        WebClient webClient = WebClient.builder()
                .baseUrl(keycloakProperties.getApiUrl())
                .defaultHeaders(h -> h.setAccept(List.of(MediaType.APPLICATION_JSON)))
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                    log.info("Request Body : {}", clientRequest.body());
                    clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
                    return Mono.just(clientRequest);
                })).defaultStatusHandler(HttpStatusCode::isError
                        , response ->
                                response.createException()
                                        .doOnNext(e -> log.error("[Keycloak API ERROR] Status : {}, Body : {}", response.statusCode(), e.getResponseBodyAsString()))

                )
                .build();

        client = new KeycloakAdminRestClient(webClient, keycloakProperties);
    }

    @Test
    @DisplayName("관리자권한 토큰 요청")
    void test() {
        StepVerifier.create(client.clientAccessToken())
                .assertNext(n -> assertThat(n).isNotNull().extracting(AccessToken::getAccessToken)
                        .isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("User 생성/조회/수정")
    void testUser() {
        UserRepresentation userRepresentation = getUserRepresentation("user1");
        StepVerifier.create(client.createUser(userRepresentation))
                .verifyComplete();
        StepVerifier.create(client.findUser(userRepresentation.getUsername())
                .flatMap(user -> {
                    user.singleAttribute("test", "testAttribute");
                    return client.updateUser(user);
                })
                .then(Mono.defer(() -> client.findUser(userRepresentation.getUsername())))
        ).assertNext(user -> assertThat(user.getAttributes().get("test").get(0)).isEqualTo("testAttribute"))
            .verifyComplete();

    }

    @Test
    @DisplayName("로그인 User, Password로 토큰 가져오기")
    void testUsernamePassword() {
        UserRepresentation userRepresentation = getUserRepresentation("user2");
        StepVerifier.create(client.createUser(userRepresentation)
                        .then(client.accessToken("testcontainer", client.createRequestTokenBody(userRepresentation.getUsername(), userRepresentation.getCredentials().get(0).getValue())))
                )
                .assertNext(token -> {
                    assertThat(token).isNotNull();
                    log.info("Token ::: {}", token);
                })
                .verifyComplete();
    }


    @Test
    @DisplayName("Client 조회")
    void testGetClient() {
        StepVerifier.create(client.getClientId("testcontainer-client"))
                .assertNext(s -> assertThat(s).isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("Realm Role 조회")
    void testGetRole() {
        StepVerifier.create(client.getRealmRole("offline_access"))
                .assertNext(s -> assertThat(s).isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("Realm Role 조회 데이터 없음")
    void testGetRoleNotFound() {
        StepVerifier.create(client.getRealmRole("none"))
                .expectError(NoSuchElementException.class)
                .verify();
    }

    @NotNull
    private static UserRepresentation getUserRepresentation(String username) {
        String password = "1111";
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setValue(password);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        org.keycloak.representations.idm.UserRepresentation userRepresentation = new org.keycloak.representations.idm.UserRepresentation();
        userRepresentation.setUsername(username);
        userRepresentation.setEmail(username + "@ttt.com");
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }



}