package kr.co.kindernoti.auth.message;

import com.querydsl.core.types.Predicate;
import kr.co.kindernoti.auth.configuration.KafkaConfiguration;
import kr.co.kindernoti.auth.login.ServiceType;
import kr.co.kindernoti.auth.user.User;
import kr.co.kindernoti.auth.user.repository.UserRepository;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderOptions;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;


@SpringBootTest(classes = {KafkaConfiguration.class, KafkaProperties.class})
@EnableConfigurationProperties
@EmbeddedKafka(topics = KafkaConfiguration.TOPIC_USER,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers", controlledShutdown = true)
class UserUpdateEventConsumerTest {

    @Autowired
    ReactiveKafkaConsumerTemplate<String, UserUpdateEvent> consumerTemplate;

    @MockBean
    UserRepository userRepository;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    ReactiveKafkaProducerTemplate<String, Object> producerTemplate;

    @BeforeEach
    public void createReactiveProducer() {
        Map<String, Object> producerProperties = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        SenderOptions<String, Object> options = SenderOptions.create(producerProperties);
        options = options
                .producerProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .producerProperty(JsonSerializer.ADD_TYPE_INFO_HEADERS, "false");

        producerTemplate = new ReactiveKafkaProducerTemplate<>(options);
    }

    @AfterEach
    public void tearDown() {
        producerTemplate.close();
    }

    @Test
    void testKafkaUserUpdateConsume(){
        //Test Data
        UserUpdateEventConsumer consumer = new UserUpdateEventConsumer(userRepository, consumerTemplate);

        User user = new User("test", "ttt@email.com", Set.of(ServiceType.teacher));
        UserUpdateEvent userUpdateEvent = new UserUpdateEvent("test", "eeee@email.com", Set.of("USER_TEACHER", "test"));
        Mono<User> mockData = Mono.just(user);

        //given
        given(userRepository.findOne(any(Predicate.class)))
                .willReturn(mockData);
        given(userRepository.save(any(User.class)))
                .willReturn(mockData);

        //then
        StepVerifier.create(producerTemplate.send(KafkaConfiguration.TOPIC_USER, userUpdateEvent))
                .assertNext(senderResult -> {
                    assertThat(senderResult.recordMetadata())
                            .extracting(RecordMetadata::topic)
                            .isEqualTo(KafkaConfiguration.TOPIC_USER);
                })
                .verifyComplete();

        StepVerifier.create(consumer.startKafkaConsumer())
                .expectNext(user)
                .thenCancel()
                .verify();
    }

}