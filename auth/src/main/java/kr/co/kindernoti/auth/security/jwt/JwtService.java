package kr.co.kindernoti.auth.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class JwtService {

    private final JwtProvider jwtProvider;

    private final ObjectMapper objectMapper;

    public Mono<Void> writeJwt(WebFilterExchange webFilterExchange, Object obj) {

        return Mono.defer(() -> {
            String s = jwtProvider.create(obj);
            TokenResponse tokenResponse = new TokenResponse(s);

            ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
            response.setStatusCode(HttpStatus.OK);

            HttpHeaders headers = response.getHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, s);
            headers.add("Content-Type", "application/json");

            DataBuffer wrap = null;
            try {
                wrap = webFilterExchange.getExchange().getResponse().bufferFactory()
                        .wrap(objectMapper.writeValueAsBytes(tokenResponse));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
            return response.writeWith(Mono.just(wrap));
        });
    }

    @Getter
    public static class TokenResponse {
        private final String token;

        public TokenResponse(String token) {
            this.token = token;
        }
    }
}
