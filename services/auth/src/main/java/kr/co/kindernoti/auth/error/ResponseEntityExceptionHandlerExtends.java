package kr.co.kindernoti.auth.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@RestControllerAdvice
public class ResponseEntityExceptionHandlerExtends extends ResponseEntityExceptionHandler {

    /**
     * Validate Binding Exception Handler 설정
     * 필드 오류를 {@link FieldErrorResponse) 객체로 변환하고 리턴한다.
     * @param ex the exception to handle
     * @param headers the headers to use for the response
     * @param status the status code to use for the response
     * @param exchange the current request and response
     * @return
     */
    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex, HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {

        List<FieldErrorResponse> invalidFields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> FieldErrorResponse.of(f.getField(), f.getDefaultMessage()))
                .toList();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidateErrorResponse(invalidFields)));
    }

}
