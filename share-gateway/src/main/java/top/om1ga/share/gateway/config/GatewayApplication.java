package top.om1ga.share.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 13:46
 * @description GatewayApplication
 */
@SpringBootApplication
@ComponentScan("top.om1ga")
@Slf4j
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GatewayApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("网关启动成功！！");
        log.info("网关地址：http://127.0.0.1:{}",env.getProperty("server.port"));
    }
}
