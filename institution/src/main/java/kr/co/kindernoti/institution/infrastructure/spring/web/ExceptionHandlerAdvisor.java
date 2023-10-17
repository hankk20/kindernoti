package kr.co.kindernoti.institution.infrastructure.spring.web;

import kr.co.kindernoti.institution.application.exception.NoDataFoundException;
import kr.co.kindernoti.institution.domain.model.vo.InvalidIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ExceptionHandlerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public Mono<ResponseEntity<Object>> handleNoDataFoundException(Exception ex, ServerWebExchange exchange) {
        NoDataFoundException exception = (NoDataFoundException) ex;
        DataErrorResponse dataErrorResponse = new DataErrorResponse(exception.getDataName(), exception.getData(), exception.getMessage());
        return createResponseEntity(dataErrorResponse, null, HttpStatus.NOT_FOUND, exchange);
    }

    @ExceptionHandler(InvalidIdException.class)
    public Mono<ResponseEntity<Object>> handleInvalidIdException(Exception ex, ServerWebExchange exchange) {
        InvalidIdException exception = (InvalidIdException) ex;
        DataErrorResponse dataErrorResponse = new DataErrorResponse(exception.getIdName(), exception.getData(), exception.getMessage());
        return createResponseEntity(dataErrorResponse, null, HttpStatus.BAD_REQUEST, exchange);
    }


    public record DataErrorResponse(String dataName, Object data, String message) {}
}
