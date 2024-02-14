package kr.co.kindernoti.configserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Slf4j
@Configuration
public class ConfigServerSecurityConfig {

    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(request -> request.requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }
    @Bean
    public EnvironmentLog environmentLog(SecurityProperties properties ) {
        log.info("Security User Name :: {}, Password : {}", properties.getUser().getName(), properties.getUser().getPassword());
        return new EnvironmentLog();
    }

    public static class EnvironmentLog {

    }
}
