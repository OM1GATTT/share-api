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
 * @date 2023年10月08日 14:55
 * @description Notice
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notice {
    private Long id;
    private String content;
    private Boolean showFlag;
    @JsonFormat(locale = "zh",timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
