package com.example.service;

import com.example.pojo.Result;

import java.util.List;

/**
 * @ClassName BikeStatusService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:59
 * @version: 1.0
 */
public interface BikeStatusService {
    //methods
    /**
     * 查询所有单车状态值信息
     * @return
     */
    public abstract Result selectAllBikeStatus();
}
