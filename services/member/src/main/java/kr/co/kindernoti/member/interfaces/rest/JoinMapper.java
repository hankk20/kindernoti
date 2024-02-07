package kr.co.kindernoti.member.interfaces.rest;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.interfaces.rest.dto.MemberCommand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JoinMapper {

    MemberJoinDto toDto(MemberCommand.JoinCommand joinCommand);
}
