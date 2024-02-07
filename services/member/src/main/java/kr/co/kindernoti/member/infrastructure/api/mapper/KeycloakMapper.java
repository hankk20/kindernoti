package kr.co.kindernoti.member.infrastructure.api.mapper;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.domain.TeacherInfo;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface KeycloakMapper {

    TeacherInfo patchTeacherInfo(TeacherInfo info, @MappingTarget TeacherInfo target);
}
