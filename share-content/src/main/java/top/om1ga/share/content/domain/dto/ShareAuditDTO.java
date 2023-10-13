package top.om1ga.share.content.domain.dto;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareAuditDTO {
    /**
     * 审核状态：枚举
     */
    private AuditStatusEnum auditStatusEnum;

    /**
     * 原因
     */
    private String reason;

    /**
     * 是否发布显示
     */
    private Boolean showFlag;
}
