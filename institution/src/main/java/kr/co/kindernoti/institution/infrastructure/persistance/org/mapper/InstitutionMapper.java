package kr.co.kindernoti.institution.infrastructure.persistance.org.mapper;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.InstitutionData;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface InstitutionMapper {
    InstitutionMapper INSTANCE = Mappers.getMapper(InstitutionMapper.class);

    InstitutionDto toDto(InstitutionData data);

    Institution toDomain(InstitutionData data);

    InstitutionData toData(Institution domain);

}
