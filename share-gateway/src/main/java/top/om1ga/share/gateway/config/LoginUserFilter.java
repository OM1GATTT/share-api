package top.om1ga.share.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import top.om1ga.share.gateway.util.JwtUtil;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 17:04
 * @description LoginUserFilter
 */
@Component
@Slf4j
public class LoginUserFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.contains("/admin")
                || path.contains("/hello")
                || path.contains("/user-service/user/login")
                || path.contains("/user-service/user/register")){
            log.info("不需要登录验证：{}",path);
            return chain.filter(exchange);
        }else{
            log.info("需要登录验证：{}",path);
        }
        String token = exchange.getRequest().getHeaders().getFirst("token");
        log.info("会员登录验证开始，token: {}",token);
        if (token == null || token.isEmpty()){
            log.info("token 为空，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 校验 token 是否有效，包括 token 是否被改过，是否过期
        boolean validate = JwtUtil.validate(token);
        if (validate){
            log.info("token 有效，放行该请求");
            return chain.filter(exchange);
        }else {
            log.warn("token 无效，请求被拦截");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
