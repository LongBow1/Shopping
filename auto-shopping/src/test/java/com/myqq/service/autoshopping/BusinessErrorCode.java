package com.myqq.service.autoshopping;

/**
 * 异常码
 *
 * @author cdliujianming
 * @date 2018/6/18 下午3:34
 */
public enum BusinessErrorCode {

    SUCCESS("0000", "OK"),
    NO_AUTH("1000", "无权限"),
    UNKOWN_ERROR("1001", "未知错误"),
    PARAM_ERROR("1002", "参数错误"),
    RUNTIME_ERROR("1003", "运行错误"),
    INCREASE_SYNC_ERROR("1004","增量同步ES没有待执行的计划"),
    INCREASE_SYNC_FOR_DELIVER_CROWD_ERROR("1005","增量同步ES没有待执行的计划"),
    CUSTOMIZED_COUPON_INFO_ALREADY_EXIST("1006","自定义优惠券信息已经存在"),
    COUPON_UNIQUE("1007","优惠券唯一性校验"),
    OPERATE_NOT_SUPPORT("1008", "不支持该操作");

    private final String code;
    private final String description;

    private BusinessErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("[%s]%s", this.code, this.description);
    }
}
