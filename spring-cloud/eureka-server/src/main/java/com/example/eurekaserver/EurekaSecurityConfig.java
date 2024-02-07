package com.example.eurekaserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
public class EurekaSecurityConfig {



    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity security) throws Exception {
        return security.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**"))
                .cors(AbstractHttpConfigurer::disable)
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
