package kr.co.kindernoti.auth.message;

import com.querydsl.core.types.dsl.BooleanExpression;
import kr.co.kindernoti.auth.user.QUser;
import kr.co.kindernoti.auth.user.User;
import kr.co.kindernoti.auth.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserUpdateEventConsumer {

    private final UserRepository userRepository;
    private final ReactiveKafkaConsumerTemplate<String, UserUpdateEvent> template;

    @EventListener(ApplicationStartedEvent.class)
    public Flux<User> startKafkaConsumer() {
        return template
                .receiveAutoAck()
                // .delayElements(Duration.ofSeconds(2L)) // BACKPRESSURE
                .map(ConsumerRecord::value)
                .transform(this::update);

    }

    public Flux<User> update(Flux<UserUpdateEvent> userFlex) {
        return userFlex.flatMap(user -> {
            BooleanExpression expression = QUser.user.userId.eq(user.getUserId());
            return userRepository.findOne(expression)
                    .flatMap(findUser -> {
                        findUser.setEmail(user.getEmail());
                        findUser.setAuthorities(user.getAuthorities());
                        return userRepository.save(findUser);
                    });
            });
    }


}
