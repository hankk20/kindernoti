package kr.co.kindernoti.member.infrastructure.spring.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * AccessToken 정보를 Json 형식으로 응답한다.
 */
@RequiredArgsConstructor
@Component
public class AccessTokenWriter {

    private final ObjectMapper objectMapper;
    public Mono<Void> writeAccessToken(ServerWebExchange serverWebExchange, AccessTokenValue value) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        try {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            DataBuffer wrap = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(
                    Map.of("access_token", value.accessToken()
                            , "refresh_token", value.refreshToken())
            ));
            return response.writeWith(Mono.just(wrap));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }

    public record AccessTokenValue(String accessToken, String refreshToken) {}

}
