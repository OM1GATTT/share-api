package top.om1ga.share.content.domain.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.om1ga.share.content.domain.entity.Share;

import java.util.Date;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月12日 09:39
 * @description ShareResp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShareResp {
    private Share share;
    private String nickname;
    private String avatarUrl;
}
