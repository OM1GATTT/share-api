package top.om1ga.share.user.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.om1ga.share.user.mapper.UserMapper;

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
}
