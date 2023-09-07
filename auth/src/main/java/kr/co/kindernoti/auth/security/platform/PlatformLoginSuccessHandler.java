package kr.co.kindernoti.auth.security.platform;

import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.filter.QueryParamConverterUtils;
import kr.co.kindernoti.auth.security.filter.QueryParamSavedCache;
import kr.co.kindernoti.auth.security.jwt.JwtService;
import kr.co.kindernoti.auth.user.QUser;
import kr.co.kindernoti.auth.user.repository.PlatformUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class PlatformLoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final PlatformUserRepository platformUserRepository;

    private final QueryParamSavedCache queryParamSavedCache = new QueryParamSavedCache();

    /**
     * Session 저장된 service 파라미터를 가져와서 LoginUser에 셋팅 하고 DB에 User정보를 저장한 후
     * JWT 생성하여 리턴 한다.
     * @param webFilterExchange the exchange
     * @param authentication the {@link Authentication}
     */
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        PlatformLoginUser user = (PlatformLoginUser) authentication.getPrincipal();
        return queryParamSavedCache.getParam(webFilterExchange.getExchange(), QueryParamConverterUtils.convertorServiceType())
                .map(serviceType -> updateServiceType(user.getUserId(), serviceType)
                                .then(Mono.just(user))
                ).flatMap(pUser -> jwtService.writeJwt(webFilterExchange, pUser));
    }

    private Mono<Void> updateServiceType(String userId, ServiceType serviceType) {
        return platformUserRepository.findOne(QUser.user.userId.eq(userId))
                .map(user -> {
                    if(user.containServiceType(serviceType)){
                        return Mono.empty();
                    }
                    user.addServiceType(serviceType);
                    return platformUserRepository.save(user)
                            .map(u -> Mono.empty());
                }).then(Mono.empty());
    }
}
