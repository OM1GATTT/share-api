package top.om1ga.share.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.om1ga.share.common.exception.BusinessException;
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

    /**
     * 系统异常统一处理
     * @param e Exception
     * @return CommonResp
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(Exception e){
        CommonResp<?> resp = new CommonResp<>();
        log.error("系统异常", e);
        resp.setSuccess(false);
        resp.setMessage(e.getMessage());
        return resp;
    }

    /**
     * 业务异常统一处理
     * @param e BusinessException
     * @return CommonResp
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BusinessException e){
        CommonResp<?> resp = new CommonResp<>();
        log.error("业务异常", e);
        resp.setSuccess(false);
        resp.setMessage(e.getE().getDesc());
        return resp;
    }

    /**
     * 数据校验统一处理
     * @param e BindException
     * @return CommonResp
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResp<?> exceptionHandler(BindException e){
        CommonResp<?> resp = new CommonResp<>();
        log.error("校验异常：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        resp.setSuccess(false);
        resp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return resp;
    }
}
