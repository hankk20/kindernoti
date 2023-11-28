package kr.co.kindernoti.configserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class ConfigServerSecurityConfig {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
