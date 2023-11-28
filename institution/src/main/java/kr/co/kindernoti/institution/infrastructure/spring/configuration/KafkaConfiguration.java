package kr.co.kindernoti.institution.infrastructure.spring.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class KafkaConfiguration {

    public static final String TOPIC_USER = "user";
    @Bean
    public NewTopic userAuthoritiesTopic() {
        return TopicBuilder
                .name(TOPIC_USER)
                .replicas(1)
                .partitions(1)
                .build();
    }

    @Bean
    public ReactiveKafkaProducerTemplate<String, Object> reactiveKafkaProducerTemplate(KafkaProperties properties) {
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(properties.buildProducerProperties()));
    }
}
