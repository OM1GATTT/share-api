package top.om1ga.share.user.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 10:23
 * @description UserApplication
 */
@SpringBootApplication
@ComponentScan("top.om1ga")
@MapperScan("top.om1ga.share.*.mapper")
@Slf4j
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(UserApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("User模块启动成功！！");
        log.info("测试地址：http://127.0.0.1:{}{}/hello",env.getProperty("server.port"),env.getProperty("server.servlet.context-path"));
    }
}
