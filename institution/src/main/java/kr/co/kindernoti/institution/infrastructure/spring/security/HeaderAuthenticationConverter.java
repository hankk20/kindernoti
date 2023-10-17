package kr.co.kindernoti.institution.infrastructure.spring.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.kindernoti.institution.domain.model.vo.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class HeaderAuthenticationConverter implements ServerAuthenticationConverter {

    private final ObjectMapper objectMapper;

    private final String AUTHORIZE_HEADER_NAME = "x-auth";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.fromCallable(() -> resolveFromHeader(exchange.getRequest()))
                .map(HeaderAuthenticationToken::new);
    }

    public Account resolveFromHeader(ServerHttpRequest request) {

        String json = request.getHeaders()
                .getFirst(AUTHORIZE_HEADER_NAME);

        if(StringUtils.isBlank(json)) {
            log.error("[auth error] 인증헤더에 값이 없습니다.");
            throw new BadCredentialsException("인증정보가 올바르지 않습니다.");
        }

        try {
            return objectMapper.readValue(json, Account.class);
        } catch (JsonProcessingException e) {
            log.error("[auth error] 인증헤더 정보가 올바르지 않습니다. [{}]", json);
            throw new BadCredentialsException("인증정보가 올바르지 않습니다.", e);
        }
    }
}
