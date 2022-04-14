package com.example.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName Bike
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 21:00
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bike {
    //properties
    private Integer id;//ID序号
    private Integer typeid;//单车类型编号
    private String bno;//单车编号[UUID]
    private Integer stopid;//所在停车区域编号
    private Integer status;//单车状态编号
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startdate;//投入使用日期编号
//    @JsonSerialize(using = GeometrySerializer.class)
//    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private String location;//单车位置属性-geometry
    private BikeType bikeType;//单车类型
    private BikeStatus bikeStatus;//单车状态
    private Stop stop;//停车点
    private City city;//所在城市


    //methods
}
