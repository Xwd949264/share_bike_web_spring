package com.example.controller;

import com.example.pojo.BikeType;
import com.example.pojo.Result;
import com.example.service.BikeTypeService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BikeController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 22:06
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/biketype")
public class BikeTypeController {
    //properties
    @Autowired
    private BikeTypeService bikeTypeService;


    /**
     * 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(){
        return ResultUtil.error(-1,"系统异常");
    }

    //methods

    /**
     * 增加BikeType
     * @param type
     * @param description
     * @param price
     * @param limits
     * @return
     */
    @RequestMapping(value = "/insert")
    @ResponseBody
    public Result insertBikeType(@RequestParam(value = "type")String type,
                                 @RequestParam(value = "description")String description,
                                 @RequestParam(value = "price") BigDecimal price,
                                 @RequestParam(value = "limits") Float limits,
                                 @RequestParam(value = "photo")MultipartFile photo) throws IOException {
//        System.out.println(type);
//        System.out.println(description);
//        System.out.println(price);
//        System.out.println(limits);
//        System.out.println(photo);
        //创建BikeType对象
        if (photo!=null){
            BikeType bikeType=new BikeType(null,type,description,price,limits,photo.getBytes());
            return bikeTypeService.insertBikeType(bikeType);
        }else {
            BikeType bikeType=new BikeType(null,type,description,price,limits,null);
            return bikeTypeService.insertBikeType(bikeType);
        }
    }

    /**
     * 检索BikeTupe
     * @return
     */
    @RequestMapping(value = "/get/all")
    @ResponseBody
    public Result selectBikeType(){
        return bikeTypeService.selectBikeType();
    }

    /**
     * 查询每种单车的数量
     * @return
     */
    @RequestMapping(value = "/get/all/count")
    @ResponseBody
    public Result selectBikeTypeWithCount(){
        return bikeTypeService.selectBikeTypeWithCount();
    }


    /**
     * 更新BikeType
     * @param id
     * @param bikeType
     * @return
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Result updateBikeType(Integer id, BikeType bikeType){
        return bikeTypeService.updateBikeType(id, bikeType);
    }

    /**
     * 查询不同类型单车的数量
     * @return
     */
    @RequestMapping(value = "/get/group")
    @ResponseBody
    public Result selectBikeTypeGroupCount(){
        return ResultUtil.success(1,"获取单车统计数据成功",bikeTypeService.selectBikeTypeGroupCount());
    }
}
