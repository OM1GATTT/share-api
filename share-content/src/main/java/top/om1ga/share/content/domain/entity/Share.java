package top.om1ga.share.content.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月08日 20:27
 * @description Share
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Share {
    private Long id;
    private Long userId;
    private String title;
    private Boolean isOriginal;
    private String author;
    private String cover;
    private String summary;
    private Integer price;
    private String downloadUrl;
    private Integer buyCount;
    private Boolean showFlag;
    private String auditStatus;
    private String reason;

    private Date createTime;
    private Date updateTime;
}
