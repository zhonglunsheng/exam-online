package com.exam.online.common;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 16:11
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR")
    ;
    private final int code;
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
