package kr.co.kindernoti.member.infrastructure.api;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.application.dto.RoleInfo;
import kr.co.kindernoti.member.application.out.MemberPort;
import kr.co.kindernoti.member.infrastructure.api.keycloak.KeycloakAdminRestClient;
import kr.co.kindernoti.member.infrastructure.api.mapper.KeycloakMapper;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAdaptor implements MemberPort {

    private final KeycloakAdminRestClient adminRestClient;
    private final KeycloakMapper keycloakMapper;

    @Override
    public Mono<Void> createMember(MemberJoinDto memberJoinDto) {
        UserRepresentation userRepresentation = toUserRepresentation(memberJoinDto);
        return adminRestClient.createUser(userRepresentation);
    }

    public Mono<Void> addMemberRole(String userId, RoleInfo roleInfo) {
        return adminRestClient.addUserRealmRole(userId, roleInfo);
    }

    private UserRepresentation toUserRepresentation(MemberJoinDto memberJoinDto) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(memberJoinDto.getUsername());
        userRepresentation.setEmail(memberJoinDto.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.singleAttribute("fullName", memberJoinDto.getFullName());
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(memberJoinDto.getPassword());
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }

}
