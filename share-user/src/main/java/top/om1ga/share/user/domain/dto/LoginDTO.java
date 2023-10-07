package top.om1ga.share.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 14:48
 * @description LoginDTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    private String phone;
    private String password;
}
