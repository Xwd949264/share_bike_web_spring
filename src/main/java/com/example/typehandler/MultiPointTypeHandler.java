package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiPoint;

/**
 * @ClassName MultiPointTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:30
 * @version: 1.0
 */
@MappedTypes(MultiPoint.class)
public class MultiPointTypeHandler extends AbstractGeometryTypeHandler<MultiPoint> {
    //properties

    //setter

    //getter

    //constructors

    //methods
}
