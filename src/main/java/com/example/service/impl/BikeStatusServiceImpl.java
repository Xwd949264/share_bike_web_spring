package com.example.service.impl;

import com.example.mapper.BikeStatusMapper;
import com.example.pojo.BikeStatus;
import com.example.pojo.Result;
import com.example.service.BikeStatusService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @ClassName BikeStatusServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:59
 * @version: 1.0
 */
@Service
public class BikeStatusServiceImpl implements BikeStatusService{
    //properties
    @Autowired
    private BikeStatusMapper bikeStatusMapper;

    @ExceptionHandler
    public Result handlerException(){
        return ResultUtil.error(0,"系统异常");
    }

    //methods
    @Override
    public Result selectAllBikeStatus() {
        List<BikeStatus> bikeStatuses = bikeStatusMapper.selectAllBikeStatus();
        return ResultUtil.success(1,"查询成功",bikeStatuses);
    }
}
