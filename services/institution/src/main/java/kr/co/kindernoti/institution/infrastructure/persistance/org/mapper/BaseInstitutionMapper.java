package kr.co.kindernoti.institution.infrastructure.persistance.org.mapper;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.BaseInstitutionData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BaseInstitutionMapper {
    BaseInstitutionMapper INSTANCE = Mappers.getMapper(BaseInstitutionMapper.class);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    InstitutionDto toDto(BaseInstitutionData data);
}
