package top.om1ga.share.content.domain.dto;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareRequestDTO {
    private Long userId;
    private String author;
    private String title;
    private Boolean isOriginal;
    private Integer price;
    private String downloadUrl;
    private String cover;
    private String summary;
}
