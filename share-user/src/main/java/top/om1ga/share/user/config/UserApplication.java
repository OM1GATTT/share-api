package top.om1ga.share.user.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 10:23
 * @description UserApplication
 */
@SpringBootApplication
@ComponentScan("top.om1ga")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class,args);
    }
}
