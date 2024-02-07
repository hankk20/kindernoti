package kr.co.kindernoti.institution.infrastructure.messaging;

import kr.co.kindernoti.institution.application.AccountUpdateEvent;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import kr.co.kindernoti.institution.infrastructure.spring.configuration.KafkaConfiguration;
import kr.co.kindernoti.institution.testsupport.TestDataCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(classes = {AccountMessageSender.class, KafkaConfiguration.class, KafkaProperties.class})
@EnableConfigurationProperties
@EmbeddedKafka(topics = KafkaConfiguration.TOPIC_USER, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class AccountMessageSenderTest {

    @Autowired
    AccountMessageSender sender;

    @Test
    @DisplayName("카프카 메세지 전송")
    void testSend() {
        Account account = TestDataCreator.createAccount();

        StepVerifier.create(sender.send(AccountUpdateEvent.of(account)))
                .expectNextCount(1)
                .verifyComplete();
    }

}