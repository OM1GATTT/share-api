package top.om1ga.share.content.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.om1ga.share.common.interceptor.LogInterceptor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 13:53
 * @description SpringMvcConfig
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }
}
