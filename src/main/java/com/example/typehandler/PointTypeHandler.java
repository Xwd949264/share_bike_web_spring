package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.Point;

/**
 * @ClassName PointTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:27
 * @version: 1.0
 */
@MappedTypes(Point.class)
public class PointTypeHandler extends AbstractGeometryTypeHandler<Point> {
    //properties

    //methods
}
