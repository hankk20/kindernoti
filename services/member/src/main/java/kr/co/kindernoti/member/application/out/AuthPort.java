package kr.co.kindernoti.member.application.out;

import kr.co.kindernoti.member.application.dto.RoleInfo;
import reactor.core.publisher.Mono;

public interface AuthPort {

    Mono<RoleInfo> getRole(String roleName);

}
