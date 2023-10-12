package top.om1ga.share.content.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 14:48
 * @description ContentApplication
 */
@SpringBootApplication
@Slf4j
@ComponentScan("top.om1ga")
@MapperScan("top.om1ga.share.*.mapper")
@EnableFeignClients(basePackages = {"top.om1ga"})
public class ContentApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ContentApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("Content模块启动成功！！");
        log.info("测试地址：http://127.0.0.1:{}",env.getProperty("server.port"));
    }
}
