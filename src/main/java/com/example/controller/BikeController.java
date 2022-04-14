package com.example.controller;

import com.example.pojo.Result;
import com.example.service.BikeService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName BikeController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 2:43
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/bike")
public class BikeController {
    //properties
    @Autowired
    private BikeService bikeService;
    //methods

    @ExceptionHandler
    public Result handlerExceptor(){
        return ResultUtil.error(0,"查询失败");
    }


    /**
     * 查询所有的单车信息
     * @return
     */
    @RequestMapping(value = "get/all/asfeaturecollection")
    @ResponseBody
    public Result selectBikeAsHashMap(@RequestParam(value = "bno",required = false) String bno,
                             @RequestParam(value = "belong",required = false) String belong){
        return bikeService.selectBikeToHashMap(bno, belong);
    }

    /**
     * 查询所有的单车信息
     * @return
     */
    @RequestMapping(value = "get/all/aslist")
    @ResponseBody
    public Result selectBikeAsList(@RequestParam(value = "bno",required = false) String bno,
                             @RequestParam(value = "belong",required = false) String belong){
        return bikeService.selectBikeAsList(bno,belong);
    }

}
