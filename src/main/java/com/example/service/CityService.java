package com.example.service;

import com.example.pojo.City;

import java.util.List;

/**
 * @ClassName CityService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 17:43
 * @version: 1.0
 */
public interface CityService {
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
    public abstract City selectOneByCityName(String cityName);


    /**
     * 空间求交运损
     * @param json
     * @return
     */
    public abstract List<City> queryByInsect(String json);


}
