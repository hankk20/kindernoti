package kr.co.kindernoti.auth.security.oauth;

import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.security.filter.QueryParamConverterUtils;
import kr.co.kindernoti.auth.security.filter.QueryParamSavedCache;
import kr.co.kindernoti.auth.login.OauthUser;
import kr.co.kindernoti.auth.security.jwt.JwtService;
import kr.co.kindernoti.auth.user.QUser;
import kr.co.kindernoti.auth.user.repository.OauthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class OauthLoginSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final OauthUserRepository oauthUserRepository;
    private QueryParamSavedCache queryParamSavedCache = new QueryParamSavedCache();

    /**
     * Session 저장된 service 파라미터를 가져와서 LoginUser에 셋팅 하고 DB에 User정보를 저장한 후
     * JWT 생성하여 리턴 한다.
     * @param webFilterExchange the exchange
     * @param authentication the {@link Authentication}
     * @return
     */
    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        OauthLoginUser user = (OauthLoginUser) authentication.getPrincipal();
        return queryParamSavedCache.getParam(webFilterExchange.getExchange(), QueryParamConverterUtils.convertorServiceType())
                    .flatMap(service -> saveUser(user, service))
                    .flatMap(loginUser -> jwtService.writeJwt(webFilterExchange, loginUser));
    }

    private Mono<OauthLoginUser> saveUser(OauthLoginUser oauthLoginUser, ServiceType serviceType){
        OauthUser build = OauthUser.builder()
                .userId(oauthLoginUser.getUserId())
                .serviceTypes(oauthLoginUser.getServiceTypes())
                .email(oauthLoginUser.getEmail())
                .oauthProvider(oauthLoginUser.getOauthProvider())
                .build();

        return oauthUserRepository.findOne(QUser.user.userId.eq(oauthLoginUser.getUserId()))
                .flatMap(user -> {
                    if(user.containServiceType(serviceType)){
                        return Mono.just(user);
                    }
                    user.addServiceType(serviceType);
                    return oauthUserRepository.save(user);
                })
                .switchIfEmpty(oauthUserRepository.save(build))
                .then(Mono.just(oauthLoginUser));
    }
}
