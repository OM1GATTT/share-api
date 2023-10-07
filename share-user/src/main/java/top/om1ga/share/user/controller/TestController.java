package top.om1ga.share.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 10:25
 * @description TestController
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
