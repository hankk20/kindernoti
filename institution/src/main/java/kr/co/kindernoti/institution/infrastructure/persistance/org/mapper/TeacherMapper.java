package kr.co.kindernoti.institution.infrastructure.persistance.org.mapper;

import kr.co.kindernoti.institution.application.dto.InstitutionDto;
import kr.co.kindernoti.institution.domain.model.org.Institution;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.InstitutionData;
import kr.co.kindernoti.institution.infrastructure.persistance.org.model.TeacherData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    Teacher toDomain(TeacherData data);

    TeacherData toData(Teacher domain);
}
