package kr.co.kindernoti.auth.security.filter;

import kr.co.kindernoti.auth.security.InvalidServicePathException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * QueryParam으로 넘어오는 정보를 Session에 저장한다.
 */
public class QueryParamSavedCache {
    private static final String ATTRIBUTE_NAME_SERVICE = "service";

    private final String attributeName;

    public QueryParamSavedCache() {
        this(ATTRIBUTE_NAME_SERVICE);
    }

    public QueryParamSavedCache(String attributeName) {
        this.attributeName = attributeName;
    }

    public Mono<Void> saveParam(ServerWebExchange exchange) {
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        if(queryParams.containsKey(attributeName)){
            return exchange.getSession().map(WebSession::getAttributes)
                    .doOnNext(attr -> attr.put(attributeName, queryParams.getFirst(attributeName)))
                    .contextWrite((ctx) -> ctx.put(attributeName, queryParams.getFirst(attributeName)))
                    .then();
        }
        return Mono.defer(() -> Mono.error(() -> new InvalidServicePathException("잘못된 경로로 접근하셨습니다.")));
    }

    /**
     * 세션에 저장된 QueryParam 정보를 가져와서 Convertor로 타입을 변환하여 리턴한다.
     * @param exchange
     * @param convertor - 타입 변환
     * @return 세션에 저장된 정보
     * @param <R>
     */
    public <R> Mono<R> getParam(ServerWebExchange exchange, Function<Object, R> convertor) {
        return exchange.getSession()
                .map(WebSession::getAttributes)
                .filter(s -> s.containsKey(attributeName))
                .map(s -> convertor.apply(s.get(attributeName)))
                .switchIfEmpty(Mono.defer(() -> Mono.error(() -> new InvalidServicePathException("잘못된 경로로 접근하셨습니다."))));
    }

}
