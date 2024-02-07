package kr.co.kindernoti.institution.infrastructure.spring.security;

import kr.co.kindernoti.core.spring.security.MemberJwtConverter;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.domain.model.vo.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InstitutionSecurity {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        return security.authorizeExchange(exchangeSpec -> exchangeSpec
                        .pathMatchers("/actuator", "/actuator/health", "/docs/**")
                        .permitAll()
                        .pathMatchers("/manage/**").hasRole("manager")
                        .anyExchange().hasRole("teacher")
                        )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(spec -> spec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(new MemberJwtConverter())))
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
