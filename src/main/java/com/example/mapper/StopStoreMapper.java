package com.example.mapper;

import com.example.pojo.StopStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName StopStoreMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 16:02
 * @version: 1.0
 */
public interface StopStoreMapper {
    //methods

    /**
     * 添加单车存储信息
     * @param username
     * @return
     */
    public abstract Integer addStopStore(@Param(value = "username") String username,
                                         @Param(value = "stopStore") StopStore stopStore);

    /**
     * 查询详细的操作日志信息
     * @return
     */
    public abstract List<StopStore> selectAllDeatil();
}
