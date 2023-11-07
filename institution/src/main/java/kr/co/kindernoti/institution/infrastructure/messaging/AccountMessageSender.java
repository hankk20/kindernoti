package kr.co.kindernoti.institution.infrastructure.messaging;

import kr.co.kindernoti.institution.application.AccountUpdateEvent;
import kr.co.kindernoti.institution.infrastructure.spring.configuration.KafkaConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderResult;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountMessageSender {

    private final ReactiveKafkaProducerTemplate<String, Object> reactiveTemplate;

    //@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) //6.1 이하 버전에서는 Reactor 지원 안함
    public Mono<SenderResult<Void>> send(AccountUpdateEvent event) {
        return reactiveTemplate.send(KafkaConfiguration.TOPIC_USER, event.getAccount())
                .doOnSuccess(result -> log.info("Send Data :: {}", result.recordMetadata().offset()));

    }
}
