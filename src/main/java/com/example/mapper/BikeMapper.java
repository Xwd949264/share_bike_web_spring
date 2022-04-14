package com.example.mapper;

import com.example.pojo.Bike;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName BikeMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:49
 * @version: 1.0
 */
public interface BikeMapper {
    //methods

    /**
     * 添加bike
     * @param bike
     * @return
     */
    public abstract Integer insertBike(@Param(value = "bike") Bike bike,
                                       @Param(value = "geom")String geom);


    /**
     * 添加bike---批处理操作[返回值为添加的总记录数]
     * @param bikes
     * @return
     */
    public abstract Integer batchInsertBike(List<Bike> bikes);

    /**
     * 查询所有的单车信息
     * @return
     */
    public abstract List<Bike> selectBike(@Param(value = "bno") String bno,
                                          @Param(value = "belong") String belong);

    /**
     * 根据id删除Bike记录
     * @param id
     * @return
     */
    public abstract Integer deleteBikeByID(@Param(value = "id") Integer id);
}
