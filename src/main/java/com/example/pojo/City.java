package com.example.pojo;


import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

/**
 * @ClassName Tb_City
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/8 - 04 - 08 - 4:42
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class City implements Serializable {
    private static final long serialVersionUID = 7008471087772270587L;
    //properties
    private String cityid;
    private String cityname;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private MultiPolygon geom;
    private String parentid;
    @JsonSerialize(using = GeometrySerializer.class)
    @JsonDeserialize(contentUsing = GeometryDeserializer.class)
    private Point centerpoint;

    //setter

    //getter

    //constructors

    //methods
}
