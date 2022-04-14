package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.BikeMapper;
import com.example.mapper.StopMapper;
import com.example.pojo.Bike;
import com.example.pojo.City;
import com.example.pojo.Result;
import com.example.service.BikeService;
import com.example.service.CityService;
import com.example.service.StopService;
import com.example.utils.ResultUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName BikeServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:53
 * @version: 1.0
 */
@Service
public class BikeServiceImpl implements BikeService {
    //properties
    @Autowired
    private BikeMapper bikeMapper;
    @Autowired
    private CityService cityService;

    @ExceptionHandler
    public Result handlerException(){
        return ResultUtil.error(-1,"系统异常");
    }

    //methods
    @Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED)
    @Override
    public Result insertBike(Bike bike) {
        //判断是否存在位置信息
        if (bike==null)
            return ResultUtil.error(0,"添加失败");
        String location = bike.getLocation();
        if (StrUtil.isEmpty(location))
            return ResultUtil.error(0,"添加失败");
        Integer rows = bikeMapper.insertBike(bike,location.toString());
        if (rows>0)
            return ResultUtil.success(1,"添加成功");
        return ResultUtil.error(0,"添加失败");
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public Result batchInsertBike(List<Bike> bikes) {
        try{
            Integer rows = bikeMapper.batchInsertBike(bikes);
            System.out.println("rows="+rows);
            if (rows>0)
                return ResultUtil.success(1,"添加成功");
            return ResultUtil.error(0,"添加失败");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(0,"添加失败");
        }
    }

    @Override
    public Result selectBikeToHashMap(String bno,String belong) {
        try {
            GeometryJSON geometryJSON = new GeometryJSON();
            List<Bike> bikes = bikeMapper.selectBike(bno,belong);
            HashMap<String,Object> featureCollection = new HashMap<>();
            featureCollection.put("type","FeatureCollection");
            //构造List集合-转为JSON-添加到hashMap集合中
            List<Map<String,Object>> features = new ArrayList<>();
            bikes.forEach(e->{
                HashMap<String,Object> feature = new HashMap<>();
                feature.put("type","Feature");
                HashMap<String,Object> properties = new HashMap<>();
                //添加属性信息
                properties.put("id",e.getId());
                properties.put("bno",e.getBno());
                properties.put("status",e.getStatus());
                properties.put("startdate", e.getStartdate());
                properties.put("type",e.getBikeType().getType());
                properties.put("stopname",e.getStop().getStopname());
                properties.put("description",e.getBikeStatus().getDescription());
                feature.put("properties",properties);
                //添加几何信息
                try {
                    feature.put("geometry",geometryJSON.read(new StringReader(e.getLocation())));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //添加到父集合中
                features.add(feature);
            });
            //添加feature集合到FeatureCollection中
            featureCollection.put("feature",features);
            return ResultUtil.success(1,"查询成功",featureCollection);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(0,"查询失败");
        }
    }

    @Override
    public Result selectBikeAsList(String bno, String belong) {
        try {
            //获取城市信息
            City city = cityService.selectOneByCityName(belong);
            List<Bike> bikes = null;
            if (city!=null){
                bikes = bikeMapper.selectBike(bno,city.getCityid());
            }else {
                bikes = bikeMapper.selectBike(bno, belong);
            }
            List<Map<String,Object>> dataList = new ArrayList<>();
            bikes.forEach(e->{
                HashMap<String,Object> properties = new HashMap<>();
                //添加属性信息
                properties.put("id",e.getId());
                properties.put("bno",e.getBno());
                properties.put("status",e.getStatus());
                properties.put("startdate", e.getStartdate());
                properties.put("type",e.getBikeType().getType());
                properties.put("stopname",e.getStop().getStopname());
                properties.put("description",e.getBikeStatus().getDescription());
                properties.put("cityname",e.getCity().getCityname());
                properties.put("location",e.getLocation());
                dataList.add(properties);
            });
            return ResultUtil.success(1,"查询成功",dataList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(0,"查询失败");
        }
    }
}
