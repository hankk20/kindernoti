package kr.co.kindernoti.member.infrastructure.spring.security;

import kr.co.kindernoti.core.spring.security.MemberJwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final MemberOauthLoginSuccessHandler memberOauthLoginSuccessHandler;
    private final MemberFormLoginSuccessHandler memberFormLoginSuccessHandler;
    private final KeycloakUserAuthenticationManager keycloakUserAuthenticationManager;
    private final KeycloakOauth2AuthorizationRequestResolver keycloakOauth2AuthorizationRequestResolver;


    /**
     * 회원 관련 보안 설정
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        return security.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/member/**"))
                .authorizeExchange(exchangeSpec -> exchangeSpec
                                .anyExchange().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(jwtSepc -> jwtSepc.jwtAuthenticationConverter(new MemberJwtConverter())))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    /**
     * 인증관련 보안 설정
     */
    @Bean
    public SecurityWebFilterChain authenticationSecurity(ServerHttpSecurity security) {
        return security.authorizeExchange(ex -> ex.pathMatchers("/join", "/docs/**").permitAll()
                        .anyExchange().authenticated())
                .formLogin(formLogin -> formLogin.authenticationManager(keycloakUserAuthenticationManager)
                        .authenticationSuccessHandler(memberFormLoginSuccessHandler))
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2Login(oauth -> oauth.authenticationSuccessHandler(memberOauthLoginSuccessHandler)
                        .authorizationRequestResolver(keycloakOauth2AuthorizationRequestResolver)
                        .authenticationFailureHandler(new OauthLoginFailHandler())
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/docs/**", corsConfiguration);
        return urlBasedCorsConfigurationSource;
    }

}
