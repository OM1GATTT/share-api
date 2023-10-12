package top.om1ga.share.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.om1ga.share.common.exception.BusinessException;
import top.om1ga.share.common.exception.BusinessExceptionEnum;
import top.om1ga.share.common.util.JwtUtil;
import top.om1ga.share.common.util.SnowUtil;
import top.om1ga.share.user.domain.dto.LoginDTO;
import top.om1ga.share.user.domain.dto.UserAddBonusMsgDTO;
import top.om1ga.share.user.domain.entity.BonusEventLog;
import top.om1ga.share.user.domain.entity.User;
import top.om1ga.share.user.domain.resp.UserLoginResp;
import top.om1ga.share.user.mapper.BonusEventLogMapper;
import top.om1ga.share.user.mapper.UserMapper;

import java.util.Date;
import java.util.Map;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:35
 * @description UserService
 */
@Service
@Slf4j
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BonusEventLogMapper bonusEventLogMapper;

    @Transactional(rollbackFor = Exception.class)
    public void updateBonus(UserAddBonusMsgDTO userAddBonusMsgDTO){
        System.out.println(userAddBonusMsgDTO);
        // 1.为用户修改积分
        Long userId = userAddBonusMsgDTO.getUserId();
        Integer bonus = userAddBonusMsgDTO.getBonus();
        User user = userMapper.selectById(userId);
        user.setBonus(user.getBonus()+bonus);
        userMapper.update(user,new QueryWrapper<User>().lambda().eq(User::getId, userId));

        bonusEventLogMapper.insert(BonusEventLog.builder()
                .userId(userId)
                .value(bonus)
                .description(userAddBonusMsgDTO.getDescription())
                .event(userAddBonusMsgDTO.getEvent())
                .build());
        log.info("积分添加完毕...");
    }

    public Long count(){
        return userMapper.selectCount(null);
    }

    public UserLoginResp login(LoginDTO loginDTO){
        // 根据手机号查询用户
        User userDB = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone,loginDTO.getPhone()));
        // 没找到，抛出运行时异常
        if (userDB == null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_NOT_EXIST);
        }
        // 密码错误
        if (!userDB.getPassword().equals(loginDTO.getPassword())){
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR);
        }
        // 都正确，返回
        UserLoginResp userLoginResp = UserLoginResp.builder().user(userDB).build();
        //String key = "om1ga";
        //Map<String,Object> map = BeanUtil.beanToMap(userLoginResp);
        String token = JwtUtil.createToken(userLoginResp.getUser().getId(), userLoginResp.getUser().getPhone());
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    public Long register(LoginDTO loginDTO){
        // 根据手机号查询用户
        User userDB = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone,loginDTO.getPhone()));
        // 找到了，手机号被注册
        if (userDB != null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_EXIST);
        }
        // 使用雪花算法生成 id
        long id = SnowUtil.getSnowflakeNextId();
        User savedUser = User.builder()
                .id(id)
                .phone(loginDTO.getPhone())
                .password(loginDTO.getPassword())
                .nickname("新用户"+String.valueOf(id).substring(0,4))
                .roles("user")
                .avatarUrl("https://i2.100024.xyz/2023/01/26/3e727b.webp")
                .bonus(100)
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        userMapper.insert(savedUser);
        return savedUser.getId();
    }

    public User findById(Long userId){
        return userMapper.selectById(userId);
    }
}
