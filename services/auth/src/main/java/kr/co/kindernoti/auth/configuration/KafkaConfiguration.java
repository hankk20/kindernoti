package kr.co.kindernoti.auth.configuration;

import kr.co.kindernoti.auth.message.UserUpdateEvent;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    public static final String TOPIC_USER = "user";

    @Bean
    public ReactiveKafkaConsumerTemplate<String, UserUpdateEvent> reactiveKafkaConsumerTemplate(KafkaProperties properties) {

        Map<String, Object> propertiesMap = properties.buildConsumerProperties();

        propertiesMap.put(JsonDeserializer.VALUE_DEFAULT_TYPE, UserUpdateEvent.class);

        return new ReactiveKafkaConsumerTemplate<>(ReceiverOptions
                .<String, UserUpdateEvent>create(propertiesMap)
                .subscription(List.of(TOPIC_USER)));
    }

}
