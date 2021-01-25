package com.myqq.service.autoshopping;


/**
 * 业务异常类
 * Created by cdliujianming on 2018/9/28.
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    private String code;
    /**
     * 异常信息
     */
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BusinessException(BusinessErrorCode businessErrorCode) {
        super(businessErrorCode.toString());
        this.code = businessErrorCode.getCode();
        this.msg = businessErrorCode.getDescription();
    }

    public BusinessException(BusinessErrorCode businessErrorCode, String errorMessage) {
        super(businessErrorCode.toString() + "-" + errorMessage);
        this.code = businessErrorCode.getCode();
        this.msg = businessErrorCode.getDescription() + "-" + errorMessage;
    }

}
