package com.example.controller;

import com.example.pojo.Result;
import com.example.pojo.StopStore;
import com.example.service.StopService;
import com.example.service.StopStoreService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName StopStoreController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 20:25
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/stopstore")
public class StopStoreController {
    //properties
    @Autowired
    private StopStoreService stopStoreService;


    @ExceptionHandler
    public Result handlerException(){
        return ResultUtil.error(-1,"系统异常");
    }


    //methods
    /**
     * 添加单车存储信息
     * @param stopStore
     * @return
     */
    @RequestMapping(value = "/insert")
    @ResponseBody
    public Result addStopStore( StopStore stopStore){
        System.out.println(stopStore);
        if (stopStore==null)
            return ResultUtil.error(0,"投放单车操作失败!");
        if (stopStore.getUsername()==null)
            return ResultUtil.error(0,"用户未登录,投放单车操作失败");
        return stopStoreService.addStopStore(stopStore.getUsername(),stopStore);
    }

    /**
     * 查询详细的操作日志信息
     * @return
     */
    @RequestMapping(value = "/select/detail")
    @ResponseBody
    public Result selectAllDeatil(){
        return stopStoreService.selectAllDeatil();
    }
}
