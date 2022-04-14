package com.example.mapper;

import com.example.pojo.BikeStatus;

import java.util.List;

/**
 * @ClassName BikeStatusMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:55
 * @version: 1.0
 */
public interface BikeStatusMapper {
    //methods

    /**
     * 查询所有单车状态值信息
     * @return
     */
    public abstract List<BikeStatus> selectAllBikeStatus();

}
