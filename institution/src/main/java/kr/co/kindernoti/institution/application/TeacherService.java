package kr.co.kindernoti.institution.application;

import kr.co.kindernoti.institution.application.exception.InstitutionBusinessException;
import kr.co.kindernoti.institution.application.in.org.InstitutionSearchUseCase;
import kr.co.kindernoti.institution.application.in.teacher.TeacherUseCase;
import kr.co.kindernoti.institution.application.out.teacher.TeacherPort;
import kr.co.kindernoti.institution.domain.model.org.InstitutionId;
import kr.co.kindernoti.institution.domain.model.teacher.Teacher;
import kr.co.kindernoti.institution.domain.model.teacher.TeacherId;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.infrastructure.messaging.AccountMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionSynchronization;
import org.springframework.transaction.reactive.TransactionSynchronizationManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeacherService implements TeacherUseCase {

    private final TeacherPort teacherPort;
    private final InstitutionSearchUseCase institutionSearchUseCase;
    private final AccountMessageSender listener;
    private final ApplicationEventPublisher publisher;

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
     *
     * @param id      등록할려는 기관 아이디
     * @param account 사융자 계정 정보
     * @return 등록된 사용자 정보
     */
    @Override
    public Mono<Teacher> join(InstitutionId id, Account account) {
        return teacherPort.exist(id, account.getUserId())
                .flatMap(exist -> {
                    if (exist) {
                        return Mono.error(new InstitutionBusinessException("해당 기관에 이미 등록된 사용자 입니다."));
                    }
                    return institutionSearchUseCase.findById(id)
                            .flatMap(inst -> {
                                        Teacher teacher = new Teacher(inst.getId(), account);
                                        return teacherPort.save(teacher);
                                    }
                            );
                });

    }

    @Override
    @Transactional
    public Mono<Void> updateAccount(TeacherId id, Account account) {
        return teacherPort.findById(id)
                .flatMap(teacher -> {
                    teacher.setAccount(account);
                    return teacherPort.updateAccount(teacher.getId(), account)
                            .map(Teacher::getAccount);
                })
                .flatMap(updatedAccount -> TransactionSynchronizationManager.forCurrentTransaction()
                                .doOnSuccess(ts -> ts.registerSynchronization(new TransactionSynchronization() {
                                        @Override
                                        public Mono<Void> afterCommit() {
                                            AccountUpdateEvent event = AccountUpdateEvent.of(updatedAccount);
                                            return listener.send(event)
                                                    .then();
                                        }
                                    })
                                ).then()
                );
    }
}
