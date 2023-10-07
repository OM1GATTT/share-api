package top.om1ga.share.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.user.domain.dto.LoginDTO;
import top.om1ga.share.user.domain.entity.User;
import top.om1ga.share.user.service.UserService;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:36
 * @description UserController
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/count")
    public Long count(){
        return userService.count();
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginDTO loginDTO){
        return userService.login(loginDTO);
    }
}
