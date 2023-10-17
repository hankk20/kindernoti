package kr.co.kindernoti.institution.interfaces.rest.mapper;

import kr.co.kindernoti.institution.application.dto.TeacherDto;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TeacherInterfaceMapper {

    @Mapping(target = "id", expression = "java(teacher.getId().toString())")
    @Mapping(target = "institutionId", expression = "java(teacher.getInstitutionId().toString())")
    TeacherDto toDto(Teacher teacher);
}
