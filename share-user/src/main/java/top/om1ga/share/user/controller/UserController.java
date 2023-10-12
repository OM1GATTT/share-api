package top.om1ga.share.user.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.user.domain.dto.LoginDTO;
import top.om1ga.share.user.domain.entity.User;
import top.om1ga.share.user.domain.resp.UserLoginResp;
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
    public CommonResp<Long> count(){
        Long count = userService.count();
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(count);
        return commonResp;
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody LoginDTO loginDTO){
        UserLoginResp userLoginResp = userService.login(loginDTO);
        CommonResp<UserLoginResp> commonResp = new CommonResp<>();
        commonResp.setData(userLoginResp);
        return commonResp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody LoginDTO loginDTO){
        Long id = userService.register(loginDTO);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(id);
        return commonResp;
    }

    @GetMapping("/{id}")
    public CommonResp<User> getUserById(@PathVariable Long id){
        User user = userService.findById(id);
        CommonResp<User> commonResp = new CommonResp<>();
        commonResp.setData(user);
        return commonResp;
    }
}
