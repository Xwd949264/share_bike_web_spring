package com.example.pojo;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:51
 * @version: 1.0
 */
public class Result<T> implements Serializable {
    //properties
    private Integer code;//状态码
    private String msg;//提示信息
    private T data;//数据

    //setter/getter

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //methods
    public Result() {
    }
    public Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
