package top.om1ga.share.content.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月13日 14:02
 * @description ShareRequestDTO
 */
@Schema(name = "ShareRequestDTO",description = "分享投稿实体类")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequestDTO {
    @Schema(name = "userId",description = "发布者ID",type = "Long")
    private Long userId;
    @Schema(name = "author",description = "作者",type = "String")
    private String author;
    @Schema(name = "title",description = "标题",type = "String")
    private String title;
    @Schema(name = "isOriginal",description = "是否原创",type = "Boolean")
    private Boolean isOriginal;
    @Schema(name = "price",description = "价格",type = "Integer")
    private Integer price;
    @Schema(name = "downloadUrl",description = "下载链接",type = "String")
    private String downloadUrl;
    @Schema(name = "cover",description = "封面",type = "String")
    private String cover;
    @Schema(name = "summary",description = "概要",type = "String")
    private String summary;
}
