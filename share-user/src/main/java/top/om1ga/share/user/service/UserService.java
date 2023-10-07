package top.om1ga.share.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.om1ga.share.common.exception.BusinessException;
import top.om1ga.share.common.exception.BusinessExceptionEnum;
import top.om1ga.share.common.util.SnowUtil;
import top.om1ga.share.user.domain.dto.LoginDTO;
import top.om1ga.share.user.domain.entity.User;
import top.om1ga.share.user.mapper.UserMapper;

import java.util.Date;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:35
 * @description UserService
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Long count(){
        return userMapper.selectCount(null);
    }

    public User login(LoginDTO loginDTO){
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
        return userDB;
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
}
