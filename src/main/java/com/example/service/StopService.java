package com.example.service;

import com.example.pojo.Result;
import com.example.pojo.Stop;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName StopService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 23:42
 * @version: 1.0
 */
public interface StopService {
    //methods

    /**
     * 计算停车点所在城市
     * @param geometry
     * @return
     */
    public String addStopAvice(String geometry);

    /**
     * 增加停车点
     * @param stop
     * @return
     */
    public abstract Result addStop(Stop stop);


    /**
     * 根据停车点名/所属政区——查询所有停车点
     * @return
     */
    public abstract List<Stop> selectStopsByName_Belong(String stopname, String belong);

    /**
     * 根据城市分组查询Stop信息-映射为HashMap
     * @return
     */
    public abstract List<HashMap<String,Object>> selectStopsByCityGroup();

    /**
     * 根据城市分组查询Stop停车点信息——映射为HashMap
     * @return
     */
    public abstract List<HashMap<String,Object>> selectStopsGroupByCity();

    /**
     * 根据城市分组查询Stop停车点信息——映射为HashMap  selectBikesGroupByCity
     * @return
     */
    public abstract List<HashMap<String,Object>> selectBikesGroupByCity();

    /**
     * 根据id查找Stop
     * @param id
     * @return
     */
    public abstract Stop selectById(Integer id);

    /**
     * 根据ID更新Stop信息
     * @param stop
     * @return
     */
    public abstract Result updateCapacityAndSizeById(Stop stop);

}
