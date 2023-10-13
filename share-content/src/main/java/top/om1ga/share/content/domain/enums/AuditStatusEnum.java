package top.om1ga.share.content.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月13日 16:10
 * @description AuditStatusEnum
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    NOT_YET,
    PASS,
    REJECT
}
