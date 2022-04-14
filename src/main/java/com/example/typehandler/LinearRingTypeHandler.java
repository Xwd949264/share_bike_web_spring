package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.LinearRing;

/**
 * @ClassName LinearRingTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:32
 * @version: 1.0
 */
@MappedTypes(LinearRing.class)
public class LinearRingTypeHandler extends AbstractGeometryTypeHandler<LinearRing> {
    //properties

    //setter

    //getter

    //constructors

    //methods
}
