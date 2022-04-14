package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.example.pojo.Result;
import com.example.pojo.Stop;
import com.example.service.StopService;
import com.example.utils.ResultUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.locationtech.jts.geom.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName StopController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 0:10
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/stop")
public class StopController {
    //properties
    @Autowired
    private StopService stopService;

    //methods

    /**
     * 获取增加停车点的位置建议
     * @param geometry
     * @return
     */
    @RequestMapping(value = "/add/stop/advice")
    @ResponseBody
    public Result addStopAvice(@RequestParam(value = "geom") String geometry){
        try {
            return ResultUtil.success(1,"已获取建议",stopService.addStopAvice(geometry));
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(0,"获取建议失败");
        }
    }


    /**
     * 增加停车点
     * @param stop
     * @return
     */
    @RequestMapping(value = "/add/stop")
    @ResponseBody
    public Result addStop(@RequestBody  Stop stop){
        System.out.println(stop);
        /*
            接收成功
        Stop(id=null, stopno=abc15e7b-ee09-46b5-8701-6a2d0a34e12b, stopname=停车点4号, builddate=Sun Apr 10 09:18:16 CST 2022, capacity=100, size=0, belong=扬州市, geom=POLYGON ((119.422116 32.355657, 119.422116 32.357253, 119.4257 32.357253, 119.4257 32.355657, 119.422116 32.355657)))
        * */
        try {
            return stopService.addStop(stop);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }



    /**
     * 根据停车点名/所属政区——查询所有停车点
     * @param stopname
     * @param belong
     * @return
     */
    @RequestMapping(value = "/select/stop/by")
    @ResponseBody
    public Result selectStopsByName_Belong(@RequestParam(value = "stopname",required = false) String stopname,
                                           @RequestParam(value = "belong",required = false) String belong){
        try {
            List<Stop> stops = stopService.selectStopsByName_Belong(stopname, belong);
            return ResultUtil.success(1,"查询成功",stops);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }


    /**
     * 根据城市分组查询Stop信息-映射为HashMap
     * @return
     */
    @RequestMapping(value = "/select/stop/group")
    @ResponseBody
    public Result selectStopsByCityGroup(@RequestParam(value = "code",required = false) String cityCode){
        try {
            List<HashMap<String, Object>> mapList = stopService.selectStopsByCityGroup();
            if (StrUtil.isEmpty(cityCode)){
                return ResultUtil.success(1,"信息查询成功",mapList);
            }{
                //stream-Filter过滤
                List<HashMap<String, Object>> mapListFilter = mapList.stream().filter(e -> cityCode.equals(e.get("belong"))).collect(Collectors.toList());
                return ResultUtil.success(1,"信息查询成功",mapListFilter);
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }

    /**
     * 根据城市分组查询Stop停车点信息——映射为HashMap
     * @return
     */
    @RequestMapping(value = "/select/stops/group")
    @ResponseBody
    public Result selectStopsGroupByCity(){
        try {
            return ResultUtil.success(1,"信息查询成功",stopService.selectStopsGroupByCity());
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }

    /**
     * 根据城市分组查询Stop停车点信息——映射为HashMap  selectBikesGroupByCity
     * @return
     */
    @RequestMapping(value = "/select/bikes/group")
    @ResponseBody
    public Result selectBikesGroupByCity(){
        try {
            return ResultUtil.success(1,"信息查询成功",stopService.selectBikesGroupByCity());
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"系统异常");
        }
    }


}
