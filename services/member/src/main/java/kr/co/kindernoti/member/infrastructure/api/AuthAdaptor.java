package kr.co.kindernoti.member.infrastructure.api;

import kr.co.kindernoti.member.application.dto.RoleInfo;
import kr.co.kindernoti.member.application.out.AuthPort;
import kr.co.kindernoti.member.infrastructure.api.keycloak.KeycloakAdminRestClient;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthAdaptor implements AuthPort {

    private final KeycloakAdminRestClient adminRestClient;

    @Cacheable(cacheNames = "role", key = "#roleName")
    @Override
    public Mono<RoleInfo> getRole(String roleName) {
        return adminRestClient.getRealmRole(roleName);
    }

}
