package com.example.mapper;

import com.example.pojo.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName CityMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/8 - 04 - 08 - 4:56
 * @version: 1.0
 */
public interface CityMapper {
    //methods

    /**
     * 查询所有City
     * @return
     */
    public abstract List<City> selectAll();

    /**
     * 根据cityName查找City
     * @param cityName
     * @return
     */
    public abstract City selectOneByCityName(@Param(value = "cityName") String cityName);

}
