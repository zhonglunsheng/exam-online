package com.exam.online.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author zhonglunsheng
 * @Description
 * @create 2019-01-16 16:07
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    /**
     * 状态码
     */
    private int code;

    /**
     * 描述说明
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public Result(int code){
        this.code = code;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success(){
        return new Result<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> Result<T> success(String msg){
        return new Result<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> Result<T> success(T data, String msg){
        return new Result<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    public static <T> Result<T> error(){
        return new Result<T>(ResponseCode.ERROR.getCode());
    }

    public static <T> Result<T> error(String msg){
        return new Result<T>(ResponseCode.ERROR.getCode(), msg);
    }

    public static <T> Result<T> error(T data){
        return new Result<T>(ResponseCode.ERROR.getCode(), data);
    }

    public static <T> Result<T> error(T data, String msg){
        return new Result<T>(ResponseCode.ERROR.getCode(), msg, data);
    }

}
