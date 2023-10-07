package top.om1ga.share.common.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 15:20
 * @description CommonResp
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonResp<T> {
    /**
     * 业务成功或失败
     */
    private Boolean success = true;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回泛型数据，自定义类型
     */
    private T data;
}
