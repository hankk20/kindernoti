package kr.co.kindernoti.member.application.in;

import reactor.core.publisher.Mono;

public interface AuthUseCase {

    Mono<Void> addMemberRole(String userId, String roleName);
}
