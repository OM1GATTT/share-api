package top.om1ga.share.content.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月12日 15:24
 * @description ExchangeDTO
 */
@Schema(name = "ExchangeDTO",description = "兑换分享实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeDTO {
    @Schema(name = "userId",description = "用户ID",type = "Long")
    private Long userId;
    @Schema(name = "shareId",description = "分享ID",type = "Long")
    private Long shareId;
}
