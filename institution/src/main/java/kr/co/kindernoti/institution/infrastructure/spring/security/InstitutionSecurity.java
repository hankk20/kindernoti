package kr.co.kindernoti.institution.infrastructure.spring.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
@RequiredArgsConstructor
public class InstitutionSecurity {

    private final AuthenticationWebFilter authenticationWebFilter;
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity security) {
        return security.authorizeExchange(exchangeSpec -> exchangeSpec.anyExchange().permitAll())
                .addFilterBefore(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();

    }


}
