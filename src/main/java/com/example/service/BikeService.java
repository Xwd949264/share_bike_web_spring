package com.example.service;

import com.example.pojo.Bike;
import com.example.pojo.Result;

import java.util.List;

/**
 * @ClassName BikeService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:52
 * @version: 1.0
 */
public interface BikeService {
    //methods
    /**
     * 添加bike
     * @param bike
     * @return
     */
    public abstract Result insertBike(Bike bike);

    /**
     * 添加bike
     * @param bikes
     * @return
     */
    public abstract Result batchInsertBike(List<Bike> bikes);

    /**
     * 查询所有的单车信息
     * @return
     */
    public abstract Result selectBikeToHashMap(String bno,String belong);

    /**
     * 查询所有的单车信息
     * @return
     */
    public abstract Result selectBikeAsList(String bno, String belong);
}
