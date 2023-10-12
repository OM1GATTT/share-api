package top.om1ga.share.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月12日 14:25
 * @description BonusEventLog
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BonusEventLog {
    private Long id;
    private Long userId;
    private Integer value;
    private String description;
    private String event;
    private Date createTime;
}
