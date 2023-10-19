package top.om1ga.share.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月13日 16:25
 * @description UserAddBonusMQDTO
 */
@Schema(name = "UserAddBonusMQDTO",description = "用户积分变动实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusMQDTO {
    @Schema(name = "userId",description = "用户ID",type = "Long")
    private Long userId;
    @Schema(name = "bonus",description = "积分",type = "Integer")
    private Integer bonus;
}
