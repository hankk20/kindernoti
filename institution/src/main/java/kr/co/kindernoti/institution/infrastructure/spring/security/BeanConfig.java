package kr.co.kindernoti.institution.infrastructure.spring.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

@Configuration
public class BeanConfig {

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(HeaderAuthenticationConverter converter) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(new CustomHeaderAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        return authenticationWebFilter;
    }
}
