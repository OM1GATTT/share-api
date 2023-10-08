package top.om1ga.share.content.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 20:26
 * @description MidUserShare
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidUserShare {
    private Long id;
    private Long shareId;
    private Long userId;
}
