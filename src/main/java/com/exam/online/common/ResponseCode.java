package com.exam.online.common;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 16:11
 */
public enum ResponseCode {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),

    /**
     * 失败
     */
    ERROR(1, "ERROR")
    ;
    /**
     * 响应状态码
     */
    private final int code;
    /**
     * 响应描述
     */
    private final String desc;



    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
