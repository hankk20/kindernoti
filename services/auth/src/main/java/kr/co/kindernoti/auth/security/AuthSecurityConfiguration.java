package kr.co.kindernoti.auth.security;

import kr.co.kindernoti.auth.security.filter.QueryParamSavedFilter;
import kr.co.kindernoti.auth.security.oauth.OauthLoginSuccessHandler;
import kr.co.kindernoti.auth.security.platform.PlatformLoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;

@RequiredArgsConstructor
@Configuration
public class AuthSecurityConfiguration {

    private final OauthLoginSuccessHandler oauthLoginSuccessHandler;
    private final PlatformLoginSuccessHandler platformLoginSuccessHandler;
    private final ReactiveClientRegistrationRepository reactiveClientRegistrationRepository;
    private final static String LOGIN_PATH = "/login";

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
//                .authorizeExchange(ex -> ex.pathMatchers("/login/**", "/join", "/.well-known/**", "/docs/**")
//                                .permitAll()
//                                .anyExchange().authenticated()
//                        )
                .authorizeExchange(ex -> ex.anyExchange().permitAll())
                .oauth2Login(oAuth2LoginSpec ->
                        oAuth2LoginSpec.authenticationSuccessHandler(oauthLoginSuccessHandler)
                                .authorizationRequestResolver(this.authorizationRequestResolver())
                )
                .formLogin(form ->
                        form.requiresAuthenticationMatcher(this.formLoginRequestMatcher())
                                .authenticationSuccessHandler(platformLoginSuccessHandler)
                ).csrf(csrf -> csrf.disable())
                .addFilterAt(this.queryParamSavedFilter(), SecurityWebFiltersOrder.FIRST)
                .build();
    }

    /**
     * Oauth 시작 경로를 login으로 맞추기 위해 재설정 함
     * @return
     */
    private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver() {
        return new DefaultServerOAuth2AuthorizationRequestResolver(
                reactiveClientRegistrationRepository, this.oauthLoginRequestMatcher());
    }

    /**
     * Oauth 로그인 경로를 /login 시작으로 설정함
     * @return
     */
    private ServerWebExchangeMatcher oauthLoginRequestMatcher() {
        return new PathPatternParserServerWebExchangeMatcher(LOGIN_PATH
                + DefaultServerOAuth2AuthorizationRequestResolver.DEFAULT_AUTHORIZATION_REQUEST_PATTERN);
    }

    /**
     * Form Login 경로는 /login/form으로 설정함
     * @return
     */
    private ServerWebExchangeMatcher formLoginRequestMatcher() {
        return new PathPatternParserServerWebExchangeMatcher(LOGIN_PATH+"/form");
    }

    /**
     * 인증서버에서 모든 서비스에 대한 검증이 이뤄지므로 각 서비스에서 인증 요청시 어느 서비스에서 요청 했는지 구분이 필요하다.
     * QueryParam으로 service 명으로 각 서비스를 구분하고 Filter에서는 각(Oauth2, FormLogin) 요청시 QueryParam을 세션에 저장한다.
     * @return
     */
    private QueryParamSavedFilter queryParamSavedFilter() {
        return new QueryParamSavedFilter(new OrServerWebExchangeMatcher(
                this.oauthLoginRequestMatcher(),
                this.formLoginRequestMatcher()));
    }
}
