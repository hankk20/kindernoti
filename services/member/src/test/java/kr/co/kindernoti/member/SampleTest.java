package kr.co.kindernoti.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.authorization.client.resource.AuthorizationResource;
import org.keycloak.authorization.client.resource.ProtectedResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.keycloak.representations.idm.authorization.Permission;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SampleTest {

    private static String clientSecret = "LtNpLsXfkOd4lZQXgL2KT4p7jpaetwc4";
    private static String loginUrl ="http://localhost:9999/realms/kindernoti/protocol/openid-connect/token";

    private static String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwS1ludlJKeFA1VWJPNXBGUTVWUzNmcFpmR1U3SmZxMWU2N1VEQlBIZE40In0.eyJleHAiOjE3MDU2NDI4MzgsImlhdCI6MTcwNTY0MjUzOCwianRpIjoiZGI4MWE2MzAtZjcyYy00NTI5LTgwNjUtNmFlY2NhNzRlYmQxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5OTk5L3JlYWxtcy9raW5kZXJub3RpIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImNmYTlkNDhlLTk4OTAtNGYwMy04MmU2LTQ1NzYyY2QzZDg0YyIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlYWNoZXJfY2xpZW50Iiwic2Vzc2lvbl9zdGF0ZSI6ImJhYzI0ZmRiLWJmMTEtNGJlYi1hY2E3LTI3NTIyYzgwYTQ2YiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovL2xvY2FsaG9zdDo4MDgwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1raW5kZXJub3RpIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsiYWNjb3VudCI6eyJyb2xlcyI6WyJtYW5hZ2UtYWNjb3VudCIsIm1hbmFnZS1hY2NvdW50LWxpbmtzIiwidmlldy1wcm9maWxlIl19fSwic2NvcGUiOiJwaG9uZSBwcm9maWxlIHRlYWNoZXIgZW1haWwgcm9sZXMiLCJzaWQiOiJiYWMyNGZkYi1iZjExLTRiZWItYWNhNy0yNzUyMmM4MGE0NmIiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsImdyYWRlIjoiMiIsInBob25lX251bWJlciI6IjAxMDAwMDAwMDAwIiwicHJlZmVycmVkX3VzZXJuYW1lIjoiaGFuazYiLCJjbGFzcyI6IjEiLCJlbWFpbCI6Imhhbms2QHR0dC50ZXN0In0.Ihla_zoOWFyB0cQ_bpccidfMn8HpddnmsmVwvrCosEYhUOWGI7YYnBk1GB4u5AXMn7RO_-E558fK3P4lg922GE0SlgOJp8JNIvMH-hSXRsMDEge2VB3E28tBFbjxFJrG2L2t4C2FiVgFFVeCp_RDXbYHt0JPEhGbLILZ3Np85Zmglb6bYRXx9phlnZP67jxeBHaTsaiCE5lFBLylJjGeU2Ve655srJDY5bbNg1NXCQEMBMMCPon7JIo7Cb4W0RKwinITdUhrSZKBnsUhLg3GNb08MGvO5aCikVhdVp2klZEkl1j8d1JH3GTLdMnJ0qfEOVV5Jhr1_39fvQwb_Q8joA";

    @DisplayName("인가처리")
    void testAuth() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/keycloak.json");
        AuthzClient client = AuthzClient.create(classPathResource.getInputStream());
        AuthorizationResource authorization = client.authorization("teacher1", "1111");
        AuthorizationResponse authorize = authorization.authorize();
        System.out.println(authorize);
        TokenIntrospectionResponse requestingPartyToken = client.protection().introspectRequestingPartyToken(authorize.getToken());
        ProtectedResource resource = client.protection().resource();
        System.out.println("Token status is: " + requestingPartyToken.getActive());
        System.out.println("Permissions granted by the server: ");
        for (Permission granted : requestingPartyToken.getPermissions()) {
            System.out.println(granted);
        }

    }

    @DisplayName("토큰 요청")
    void requestToken() {
        WebClient build = WebClient.builder().baseUrl(loginUrl).build();
        ResponseEntity<String> block = build.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", "teacher-client")
                        .with("client_secret", clientSecret)
                        .with("username", "hank6")
                        .with("password", "1234"))
                .exchangeToMono(s -> s.toEntity(String.class))
                .block();
        assert block != null;
        System.out.println(block);
        assertThat(block.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("권한토큰요청")
    void testAccess() throws IOException {
        WebClient build = WebClient.builder().baseUrl(loginUrl).build();
        ResponseEntity<String> block = build.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization" , "Bearer "+jwt)
                .body(BodyInserters.fromFormData("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket")
                        .with("audience", "teacher-client")
                )
                .exchangeToMono(s -> s.toEntity(String.class))
                .block();
        assert block != null;
        System.out.println(block);
        assertThat(block.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthzClient client = AuthzClient.create();
        System.out.println(">>>>> "+client.authorization(jwt).authorize().getToken());

    }

    @DisplayName("사용자 생성")
    void createUser() throws JsonProcessingException {

        Keycloak instance = Keycloak.getInstance("http://localhost:9999", "master", "admin", "admin", "admin-cli");
        RealmResource kindernoti = instance.realm("kindernoti");
        UsersResource users = kindernoti.users();
        UserRepresentation userRepresentation = new UserRepresentation();
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue("1234");
        userRepresentation.setUsername("hank6");
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail("hank6@ttt.test");
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        userRepresentation.setAttributes(attr());
        Response response = users.create(userRepresentation);
        System.out.println("Result ::: "+response.getStatus()+" >>>>"+response.readEntity(String.class));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    void keycloakBuilder() {
        Keycloak build = KeycloakBuilder.builder()
                .realm("master")
                .serverUrl("http://localhost:9999")
                .username("admin")
                .password("admin")
                .clientId("admin-cli")
                .build();
        RealmResource kindernoti = build.realm("kindernoti");
        UsersResource users = kindernoti.users();
        System.out.println(users.count());
        List<UserRepresentation> list = users.list();
        for (UserRepresentation userRepresentation : list) {
            System.out.println(userRepresentation);
        }
    }

    public static Map<String, List<String>> attr() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        InsitutionInfo insitutionInfo = new InsitutionInfo("inst_1", "grad_2", "class_3");
        HashMap<String, List<String>> map = new HashMap<>();
        List<String> strings = List.of("01011112222", "01022223333");
        map.put("phoneNumber", List.of("01000000000"));
        map.put("institution", List.of("12313"));
        map.put("grade", List.of("2"));
        map.put("class", List.of("1"));
        map.put("institutionInfo",List.of(objectMapper.writeValueAsString(insitutionInfo)));
        return map;
    }

    public static class InsitutionInfo {
        private String institutionId;
        private String gradeId;
        private String classId;

        public InsitutionInfo(String institutionId, String gradeId, String classId) {
            this.institutionId = institutionId;
            this.gradeId = gradeId;
            this.classId = classId;
        }

        public String getInstitutionId() {
            return institutionId;
        }

        public void setInstitutionId(String institutionId) {
            this.institutionId = institutionId;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }
    }
}
