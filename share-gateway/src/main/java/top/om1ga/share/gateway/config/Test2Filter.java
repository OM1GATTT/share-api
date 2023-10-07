package top.om1ga.share.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 16:57
 * @description Test2Filter
 */
@Component
@Slf4j
public class Test2Filter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Test2Filter");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}