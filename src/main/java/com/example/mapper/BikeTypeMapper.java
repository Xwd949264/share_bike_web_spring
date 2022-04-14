package com.example.mapper;

import com.example.pojo.BikeType;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BikeType
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:04
 * @version: 1.0
 */
public interface BikeTypeMapper {
    //methods

    /**
     * 增加BikeType
     * @param bikeType
     * @return
     */
    public abstract Integer insertBikeType(@Param(value = "bikeType") BikeType bikeType);

    /**
     * 检索BikeTupe
     * @return
     */
    public abstract List<BikeType> selectBikeType();

    /**
     * 查询每种单车的数量
     * @return
     */
    public abstract List<HashMap<String,Object>> selectBikeTypeWithCount();

    /**
     * 更新BikeType
     * @param id
     * @param bikeType
     * @return
     */
    public abstract Integer updateBikeType(@Param(value = "id") Integer id,
                                           @Param(value = "bikeType") BikeType bikeType);

    /**
     * 查询不同类型单车的数量
     * @return
     */
    public abstract List<HashMap<String,Object>> selectBikeTypeGroupCount();

}
