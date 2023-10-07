package top.om1ga.share.common.exception;

/**
 * @author OM1GA
 * @version 1.0
 * @date 2023年10月07日 15:48
 * @description BusinessException
 */
public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum e;

    public BusinessException(BusinessExceptionEnum e){
        this.e = e;
    }

    public void setE(BusinessExceptionEnum e){
        this.e = e;
    }

    public BusinessExceptionEnum getE() {
        return e;
    }
}
