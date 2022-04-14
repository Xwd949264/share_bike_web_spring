package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import com.example.mapper.CityMapper;
import com.example.pojo.City;
import com.example.pojo.Result;
import com.example.service.CityService;
import com.example.utils.ResultUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CityServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 17:43
 * @version: 1.0
 */
@Service
public class CityServiceImpl implements CityService {
    //properties
    @Autowired
    private CityMapper cityMapper;

    //methods
    @Override
    public List<City> selectAll() {
        return cityMapper.selectAll();
    }

    @Override
    public City selectOneByCityName(String cityName) {
        try{
            if (StrUtil.isEmpty(cityName))
                return null;
            return cityMapper.selectOneByCityName(cityName);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<City> queryByInsect(String json) {
        try {
            //类型转换
            GeometryJSON geometryJSON = new GeometryJSON();
            Geometry geometryParam = geometryJSON.read(new StringReader(json));
            //空间求交运算结果
            List<City> data = new ArrayList<>();
            //获取city列表
            List<City> cities = this.selectAll();
            //空间求交运算
            cities.forEach(e->{
                if (e.getGeom()!=null&&e.getGeom().intersects(geometryParam)){
                    data.add(e);
                }
            });
            //返回结果
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
