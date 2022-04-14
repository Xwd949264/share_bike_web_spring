package com.example.service.impl;

import com.example.mapper.StopMapper;
import com.example.pojo.City;
import com.example.pojo.Result;
import com.example.pojo.Stop;
import com.example.service.CityService;
import com.example.service.StopService;
import com.example.utils.ResultUtil;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName StopServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 23:42
 * @version: 1.0
 */
@Service
public class StopServiceImpl implements StopService {
    //properties
    @Autowired
    private StopMapper stopMapper;
    @Autowired
    private CityService cityService;


    //methods
    @Override
    public String addStopAvice(String geometryJson) {
        //GeometryJSON:Reads and writes geometry objects to and from geojson
        GeometryJSON geometryJSON = new GeometryJSON();
        try {
            //创建Geometry对象
            Geometry geometry = geometryJSON.read(new StringReader(geometryJson));
            //获取城市列表
            List<City> cities = cityService.selectAll();
            //获取所在城市
            for (int i = 0; i < cities.size(); i++) {
                City city = cities.get(i);
                MultiPolygon geom = city.getGeom();
                if (geom.intersects(geometry))
                    return city.getCityname();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Result addStop(Stop stop) {
        try{
            //查询CityName
            City city = cityService.selectOneByCityName(stop.getBelong());
            if (city!=null){
                //设置CityId
                stop.setBelong(city.getCityid());
            }
            //添加Stop
            Integer rows = stopMapper.addStop(stop, stop.getGeom().toString());
            if (rows>0){
                return ResultUtil.success(1,"新增停车区域成功");
            }else {
                return ResultUtil.error(0,"新增停车区域失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(0,"新增停车区域失败");
        }
    }

    @Override
    public List<Stop> selectStopsByName_Belong(String stopname, String belong) {
        try {
            return stopMapper.selectStopsByName_Belong(stopname, belong);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HashMap<String, Object>> selectStopsByCityGroup() {
        try {
            return stopMapper.selectStopsByCityGroup();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HashMap<String, Object>> selectStopsGroupByCity() {
        try{
            return stopMapper.selectStopsGroupByCity();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<HashMap<String, Object>> selectBikesGroupByCity() {
        try{
            return stopMapper.selectBikesGroupByCity();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Stop selectById(Integer id) {
        try {
            if (id==null)
                return null;
            return stopMapper.selectById(id);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ,propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    @Override
    public Result updateCapacityAndSizeById(Stop stop) {
        try {
            if (stop==null)
                return ResultUtil.error(0,"更新停车点信息失败");
            if (stop.getId()==null||stop.getSize()==null||stop.getCapacity()==null)
                return ResultUtil.error(0,"参数错误,更新失败");
            //判断当前stop容量是否可用
            Stop stopById = this.selectById(stop.getId());
            if (stopById==null){
                return ResultUtil.error(0,"停车点不存在,更新失败");
            }
            //判断容量
            if (stopById.getCapacity()<stop.getSize()||stop.getCapacity()<stopById.getSize())
                return ResultUtil.error(0,"参数错误,更新失败");
            //执行更新操作
            Integer rows = stopMapper.updateCapacityAndSizeById(stop);
            if (rows>0)
                return ResultUtil.error(1,"停车点信息更新成功");
            return ResultUtil.error(0,"更新停车点信息失败");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }
}
