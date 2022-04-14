package com.example.service;

import com.example.pojo.Result;
import com.example.pojo.StopStore;

import java.util.List;

/**
 * @ClassName StopStoreService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:09
 * @version: 1.0
 */
public interface StopStoreService {
    //methods

    /**
     * 添加单车存储信息
     * @param username
     * @param stopStore
     * @return
     */
    public abstract Result addStopStore(String username, StopStore stopStore);

    /**
     * 查询详细的操作日志信息
     * @return
     */
    public abstract Result selectAllDeatil();
}
