package kr.co.kindernoti.member.application.in;

import kr.co.kindernoti.member.application.dto.MemberJoinDto;
import kr.co.kindernoti.member.application.dto.TeacherMemberDto;
import reactor.core.publisher.Mono;

public interface JoinUseCase {

    Mono<Void> join(MemberJoinDto memberJoinDto);

}
