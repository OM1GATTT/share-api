package top.om1ga.share.content.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:27
 * @description User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String phone;
    private String password;
    private String nickname;
    private String roles;
    private String avatarUrl;
    private Integer bonus;
    private Date createTime;
    private Date updateTime;
}
