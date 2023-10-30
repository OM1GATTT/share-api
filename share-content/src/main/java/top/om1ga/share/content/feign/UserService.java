package top.om1ga.share.content.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import top.om1ga.share.common.resp.CommonResp;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月12日 09:24
 * @description UserService
 */
@FeignClient(value = "user-service",path="/user")
public interface UserService {

    /**
     * 调用用户中心查询用户信息接口
     * @param id 用户ID
     * @return CommonResp<User>
     */
    @GetMapping("/{id}")
    CommonResp<User> getUser(@PathVariable Long id);

    @PutMapping("/update-bonus")
    CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO);

    @PostMapping("/updateAvatar")
    CommonResp<String> updateAvatar(@RequestHeader(value = "token",required = false) String token,@RequestParam String avatarUrl);
}
