package com.example.utils;

import com.example.pojo.Result;

/**
 * @ClassName ResultUtil
 * @Description: com.example.utils
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 10:02
 * @version: 1.0
 */
public class ResultUtil {
    //properties
    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static Result success(Integer code,String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static Result success(Integer code,String msg,Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
