package kr.co.kindernoti.institution.interfaces.rest.mapper;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionCommand;
import kr.co.kindernoti.institution.interfaces.rest.dto.InstitutionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InstitutionInterfaceMapper {

    InstitutionInterfaceMapper INSTANCE = Mappers.getMapper(InstitutionInterfaceMapper.class);

    @Mapping(target = "id", expression = "java(data.getId().toString())")
    InstitutionResponse toResponse(InstitutionDto data);

    @Mapping(target = "id", expression = "java(domain.getId().toString())")
    InstitutionResponse toResponse(Institution domain);

    Institution patchToDomain(InstitutionCommand.PatchCommand patchCommand, @MappingTarget Institution target);

}
