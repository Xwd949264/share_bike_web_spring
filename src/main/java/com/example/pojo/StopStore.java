package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName StopStore
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/11 - 04 - 11 - 15:59
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StopStore {
    //properties
    private Integer id;//ID编号
    private Integer stopid;//停车点编号
    private Integer typeid;//投放的单车类型编号
    private Integer addtion;//增加数量
    private Integer reduce;//减少数量
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operationdate;//操作时间
    private String username;//操作账户
    private Stop stop;//投放停车点
    private BikeType bikeType;//投放单车类型

    //methods
}
