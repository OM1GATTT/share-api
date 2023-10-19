package top.om1ga.share.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月12日 14:29
 * @description UserAddBonusMsgDTO
 */
@Schema(name = "UserAddBonusMsgDTO",description = "用户积分变动消息实体类")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddBonusMsgDTO {
    @Schema(name = "userId",description = "用户ID",type = "Long")
    private Long userId;
    @Schema(name = "bonus",description = "积分",type = "Integer")
    private Integer bonus;
    @Schema(name = "description",description = "描述",type = "String")
    private String description;
    @Schema(name = "event",description = "事件",type = "String")
    private String event;
}
