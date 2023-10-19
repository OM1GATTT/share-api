package top.om1ga.share.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 10:25
 * @description TestController
 */
@Tag(name = "测试")
@RestController
public class TestController {

    @Operation(summary = "hello")
    @GetMapping("/hello")
    public String hello(){
        int a = 1/0;
        return "hello world";
    }
}
