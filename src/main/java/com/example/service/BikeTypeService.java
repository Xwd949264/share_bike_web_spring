package com.example.service;

import com.example.pojo.BikeType;
import com.example.pojo.Result;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @ClassName BikeTypeService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:24
 * @version: 1.0
 */
public interface BikeTypeService {
    //methods
    /**
     * 增加BikeType
     * @param bikeType
     * @return
     */
    public abstract Result insertBikeType(BikeType bikeType);

    /**
     * 检索BikeTupe
     * @return
     */
    public abstract Result selectBikeType();

    /**
     * 查询每种单车的数量
     * @return
     */
    public abstract Result selectBikeTypeWithCount();

    /**
     * 更新BikeType
     * @param id
     * @param bikeType
     * @return
     */
    public abstract Result updateBikeType(Integer id, BikeType bikeType);

    /**
     * 查询不同类型单车的数量
     * @return
     */
    public abstract List<HashMap<String,Object>> selectBikeTypeGroupCount();
}
