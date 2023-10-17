package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.exception.AlreadyDataException;
import kr.co.kindernoti.institution.application.exception.InstitutionBusinessException;
import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.org.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.application.out.teacher.TeacherPort;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class TeacherService implements TeacherUseCase {

    private final TeacherPort teacherPort;
    private final InstitutionSearchUseCase institutionSearchUseCase;

    @Override
    public Flux<Teacher> findTeacherByUserId(String userId) {
        return teacherPort.findByUserId(userId);
    }

    @Override
    public Mono<Teacher> save(Teacher teacher) {
        return teacherPort.save(teacher);
    }

    @Override
    public Mono<Teacher> findById(TeacherId teacherId) {
        return teacherPort.findById(teacherId);
    }

    /**
     * 해당 기관에 사용자가 등록되어 있는지 확인하여
     * 등록되어 있지 않으면 사용자에 기관정보를 설정하여 신규로 등록한다.
     * @param id 등록할려는 기관 아이디
     * @param account 사융자 계정 정보
     * @return 등록된 사용자 정보
     */
    @Override
    public Mono<Teacher> join(InstitutionId id, Account account) {
        return teacherPort.exist(id, account.getUserId())
                .flatMap(exist -> {
                    if(exist) {
                        return Mono.error(new InstitutionBusinessException("해당 기관이 등록된 사용자 입니다."));
                    }
                    return institutionSearchUseCase.findById(id)
                            .flatMap(inst -> {
                                        Teacher teacher = new Teacher(inst.getId(), account);
                                        return teacherPort.save(teacher);
                                    }
                            );
                });

    }
}
