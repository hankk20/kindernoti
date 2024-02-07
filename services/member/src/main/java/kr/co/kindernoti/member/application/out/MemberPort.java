package kr.co.kindernoti.member.application.out;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.application.dto.RoleInfo;
import kr.co.kindernoti.member.domain.TeacherInfo;
import org.keycloak.representations.idm.RoleRepresentation;
import reactor.core.publisher.Mono;

public interface MemberPort {

    Mono<Void> createMember(MemberJoinDto memberJoinDto);

    Mono<Void> addMemberRole(String userId, RoleInfo roleInfo);

}
