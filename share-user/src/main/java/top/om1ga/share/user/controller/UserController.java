package top.om1ga.share.user.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.user.domain.dto.LoginDTO;
import top.om1ga.share.user.domain.dto.UserAddBonusMsgDTO;
import top.om1ga.share.user.domain.entity.BonusEventLog;
import top.om1ga.share.user.domain.entity.User;
import top.om1ga.share.user.domain.resp.UserLoginResp;
import top.om1ga.share.user.service.UserService;

import java.util.List;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:36
 * @description UserController
 */
@Tag(name = "用户中心")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Operation(summary = "积分明细")
    @Parameters({@Parameter(name = "pageNo",description = "页码",in = ParameterIn.QUERY),
            @Parameter(name = "pageSize",description = "每页个数",in = ParameterIn.QUERY),
            @Parameter(name = "token",description = "请求token",in = ParameterIn.HEADER)})
    @GetMapping("/bonus/list")
    public CommonResp<List<BonusEventLog>> getBonusList(@RequestParam(required = false,defaultValue = "1")Integer pageNo,
                                                  @RequestParam(required = false,defaultValue = "20")Integer pageSize,
                                                  @RequestHeader(value = "token",required = false) String token){
        if (pageSize>20){
            pageSize=20;
        }
        System.out.println(pageNo+","+pageSize);
        long userId = getUserIdFromToken(token);
        CommonResp<List<BonusEventLog>> commonResp = new CommonResp<>();
        commonResp.setData(userService.getBonusEventLogList(pageNo,pageSize,userId));
        return commonResp;
    }

    private long getUserIdFromToken(String token){
        log.info(">>>>>>>>>token"+token);
        long userId=0;
        String noToken="no-token";
        if(!noToken.equals(token)){
            JSONObject jsonObject = JwtUtil.getJSONObject(token);
            log.info("解析到 token的JSON数据为:{}",jsonObject);
            userId=Long.parseLong(jsonObject.get("id").toString());
        }else {
            log.info("没有token");
        }
        return userId;
    }

    @Operation(summary = "更新积分")
    @PutMapping(value = "/update-bonus")
    public CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO){
        Long userId = userAddBonusMsgDTO.getUserId();
        userService.updateBonus(
                UserAddBonusMsgDTO.builder()
                        .userId(userId)
                        .bonus(userAddBonusMsgDTO.getBonus())
                        .description("兑换分享")
                        .event("BUY")
                        .build()
        );
        CommonResp<User> commonResp = new CommonResp<>();
        commonResp.setData(userService.findById(userId));
        return commonResp;
    }

    @Operation(summary = "查询用户总数")
    @GetMapping("/count")
    public CommonResp<Long> count(){
        Long count = userService.count();
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(count);
        return commonResp;
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody LoginDTO loginDTO){
        UserLoginResp userLoginResp = userService.login(loginDTO);
        CommonResp<UserLoginResp> commonResp = new CommonResp<>();
        commonResp.setData(userLoginResp);
        return commonResp;
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody LoginDTO loginDTO){
        Long id = userService.register(loginDTO);
        CommonResp<Long> commonResp = new CommonResp<>();
        commonResp.setData(id);
        return commonResp;
    }

    @Operation(summary = "根据ID查询用户信息")
    @GetMapping("/{id}")
    @Parameters(@Parameter(name = "id",description = "用户ID",in = ParameterIn.PATH))
    public CommonResp<User> getUserById(@PathVariable Long id){
        User user = userService.findById(id);
        CommonResp<User> commonResp = new CommonResp<>();
        commonResp.setData(user);
        return commonResp;
    }
}
