package top.om1ga.share.content.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.om1ga.share.content.domain.enums.AuditStatusEnum;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月13日 16:16
 * @description ShareAuditDTO
 */
@Schema(name = "ShareAuditDTO",description = "分享审核实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareAuditDTO {

    /**
     * 审核状态：枚举
     */
    @Schema(name = "AuditStatusEnum",description = "审核装备枚举类",type = "String")
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因
     */
    @Schema(name = "reason",description = "原因",type = "String")
    private String reason;

    /**
     * 是否发布显示
     */
    @Schema(name = "showFlag",description = "是否显示",type = "Boolean")
    private Boolean showFlag;
}
