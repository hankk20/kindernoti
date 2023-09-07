package kr.co.kindernoti.auth.security.filter;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * QueryParam 정보를 세션에 저장 한다.
 */
public class QueryParamSavedFilter implements WebFilter {

    private final QueryParamSavedCache queryParamSavedCache = new QueryParamSavedCache();

    private final ServerWebExchangeMatcher serverWebExchangeMatcher;

    public QueryParamSavedFilter(ServerWebExchangeMatcher serverWebExchangeMatcher){
        this.serverWebExchangeMatcher = serverWebExchangeMatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return serverWebExchangeMatcher.matches(exchange)
                .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                .flatMap((e) -> queryParamSavedCache.saveParam(exchange))
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange)));
    }
}
