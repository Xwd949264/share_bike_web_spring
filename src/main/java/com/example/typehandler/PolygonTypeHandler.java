package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Polygon;


/**
 * @ClassName PolygonTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:25
 * @version: 1.0
 */
@MappedTypes(Polygon.class)
public class PolygonTypeHandler extends AbstractGeometryTypeHandler<Polygon> {
    //properties

    //methods
}
