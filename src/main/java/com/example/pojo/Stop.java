package com.example.pojo;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Stop
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 23:19
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stop implements Serializable {
    private static final long serialVersionUID = -476748751786817326L;
    //properties
    private Integer id;//ID
    private String stopno;//停车点编号
    private String stopname;//停车点名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date builddate;//启用日期
    private Integer capacity;//停车区容量
    private Integer size;//当前已用容量
    private String belong;//所属政区
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private Polygon geom;//停车点区域边界
    private City city;//所属区域

    //methods
}
