package com.example.service.impl;

import com.example.mapper.BikeTypeMapper;
import com.example.pojo.BikeType;
import com.example.pojo.Result;
import com.example.service.BikeTypeService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BikeTypeServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:24
 * @version: 1.0
 */
@Service
public class BikeTypeServiceImpl implements BikeTypeService {
    //properties
    @Autowired
    private BikeTypeMapper bikeTypeMapper;

    /**
     * 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(){
        return ResultUtil.error(-1,"系统异常");
    }

    //methods
    @Override
    public Result insertBikeType(BikeType bikeType) {
        Integer rows = bikeTypeMapper.insertBikeType(bikeType);
        if (rows>0)
            return ResultUtil.success(1,"已成功增加新的单车类型");
        return ResultUtil.error(0,"增加新的单车类型失败");
    }

    @Override
    public Result selectBikeType() {
        List<BikeType> bikeTypes = bikeTypeMapper.selectBikeType();
        return ResultUtil.success(1,"成功获取单车类型数据",bikeTypes);
    }

    @Override
    public Result selectBikeTypeWithCount() {
        try {
            List<HashMap<String, Object>> mapList = bikeTypeMapper.selectBikeTypeWithCount();
            return ResultUtil.success(1,"成功获取单车类型数据",mapList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }

    @Override
    public Result updateBikeType(Integer id, BikeType bikeType) {
        if (id==null||id<=0)
            ResultUtil.error(0,"参数错误,修改单车类型信息失败");
        Integer rows = bikeTypeMapper.updateBikeType(id, bikeType);
        if (rows>0)
            return ResultUtil.success(1,"修改单车类型信息成功");
       return ResultUtil.error(0,"修改单车类型信息失败");
    }

    @Override
    public List<HashMap<String, Object>> selectBikeTypeGroupCount() {
        return bikeTypeMapper.selectBikeTypeGroupCount();
    }


}
