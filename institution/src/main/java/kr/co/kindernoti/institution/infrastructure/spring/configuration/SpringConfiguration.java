package kr.co.kindernoti.institution.infrastructure.spring.configuration;

import kr.co.kindernoti.institution.infrastructure.basedata.CityCodeSaveService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@Configuration
@EnableReactiveMongoAuditing
public class SpringConfiguration {

    @Bean
    public ApplicationRunner applicationRunner(CityCodeSaveService cityCodeSaveService) {
        return (arguments) -> cityCodeSaveService.initCitiCode();
    }
}
