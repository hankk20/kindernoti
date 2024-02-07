package kr.co.kindernoti.member.application;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.application.dto.TeacherMemberDto;
import kr.co.kindernoti.member.application.in.JoinUseCase;
import kr.co.kindernoti.member.application.out.AuthPort;
import kr.co.kindernoti.member.application.out.MemberPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class JoinService implements JoinUseCase {

    private final MemberPort memberPort;
    private final AuthPort authPort;
    private final String teacherRole = "teacher";

    @Override
    public Mono<Void> join(MemberJoinDto memberJoinDto) {
        return memberPort.createMember(memberJoinDto);
    }
}
