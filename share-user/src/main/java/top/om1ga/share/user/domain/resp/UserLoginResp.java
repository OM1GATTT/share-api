package top.om1ga.share.user.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.om1ga.share.user.domain.entity.User;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 16:29
 * @description UserLoginResp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginResp {
    private User user;
    private String token;
}
