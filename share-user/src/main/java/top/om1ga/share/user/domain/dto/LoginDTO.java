package top.om1ga.share.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(name = "LoginDTO",description = "登录实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    @Schema(name = "phone",description = "手机号",type = "String")
    @NotBlank(message = "[手机号] 不能为空")
    private String phone;
    @Schema(name = "password",description = "密码",type = "String")
    private String password;
}
