package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.example.pojo.City;
import com.example.pojo.Result;
import com.example.service.CityService;
import com.example.utils.ResultUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CityController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/8 - 04 - 08 - 4:49
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/city")
public class CityController {
    //properties
    @Autowired
    private CityService cityService;

    //methods
    /**
     * 获取所有city
     * @return
     */
    @RequestMapping(value = "/GET/all")
    @ResponseBody
    public List<City> getAll(){
        Map<String,Object> map = new HashMap<>();
        return cityService.selectAll();
    }

    /**
     * 空间求交运算
     * @param area
     * @return
     */
    @RequestMapping(value = "/query/insect",method = RequestMethod.POST)
    @ResponseBody
    public Result queryByInsect(@RequestParam(value = "area") String area) throws JsonProcessingException {
        System.out.println(area);
//        Map<String,Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        if (StrUtil.isEmpty(area)){
//            map.put("code",0);
//            map.put("msg","请求参数为空");
//            map.put("data",null);
//            return mapper.writeValueAsString(map);
            ResultUtil.success(0,"请求参数为空");
        }
        //开始解析
        try {
            GeometryJSON geometryJSON = new GeometryJSON();
            Geometry geometry = geometryJSON.read(new StringReader(area));
            //通过空间求交操作,获取解析所在区域对象
            List<City> data = cityService.queryByInsect(area);
            System.out.println(data);
//            map.put("code",1);
//            map.put("msg","空间运算成功");
//            map.put("data",data);
            data.forEach(e->{
                System.out.println(e.getCityname()+"\t");
                System.out.println(e.getGeom());
            });
//            HttpMessageNotWritableException
//            mapper.registerModule(new JtsModule());
            return ResultUtil.success(1,"空间运算成功",data);
        } catch (IOException e) {
            e.printStackTrace();
//            map.put("code",0);
//            map.put("msg","请求参数格式错误,无法执行解析");
//            map.put("data",null);
//            return mapper.writeValueAsString(map);
            return ResultUtil.success(0,"请求参数格式错误,无法执行解析");
        }
    }

}
