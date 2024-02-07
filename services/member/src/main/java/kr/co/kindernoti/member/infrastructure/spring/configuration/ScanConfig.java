package kr.co.kindernoti.member.infrastructure.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"kr.co.kindernoti.member", "kr.co.kindernoti.core"})
public class ScanConfig {
}
