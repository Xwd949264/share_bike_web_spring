package com.example.mapper;

import com.example.pojo.Stop;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StopMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 23:24
 * @version: 1.0
 */
public interface StopMapper {
    //methods

    /**
     * 增加停车点
     * @param stop
     * @return
     */
    public abstract Integer addStop(@Param(value = "stop") Stop stop,
                                    @Param(value = "geom")String geom);

    /**
     * 根据停车点名/所属政区——查询所有停车点
     * @return
     */
    public abstract List<Stop> selectStopsByName_Belong(@Param(value = "stopname") String stopname,
                                                        @Param(value = "belong")String belong);

    /**
     * 根据城市分组查询Stop信息-映射为HashMap
     * @return
     */
    public abstract List<HashMap<String,Object>> selectStopsByCityGroup();

    /**
     * 根据id查找Stop
     * @param id
     * @return
     */
    public abstract Stop selectById(@Param(value = "id") Integer id);

    /**
     * 根据ID更新Stop信息
     * @param stop
     * @return
     */
    public abstract Integer updateCapacityAndSizeById(@Param(value = "stop")Stop stop);

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

}
