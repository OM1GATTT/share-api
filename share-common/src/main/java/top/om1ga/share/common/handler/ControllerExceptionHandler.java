package top.om1ga.share.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.om1ga.share.common.resp.CommonResp;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 15:36
 * @description ControllerExceptionHandler 统一异常处理类
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(Exception e){
        CommonResp<?> resp = new CommonResp<>();
        log.error("系统异常", e);
        resp.setSuccess(false);
        resp.setMessage(e.getMessage());
        return resp;
    }
}
