package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiPolygon;

/**
 * @ClassName MultiPolygonTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:31
 * @version: 1.0
 */
@MappedTypes(MultiPolygon.class)
public class MultiPolygonTypeHandler extends AbstractGeometryTypeHandler<MultiPolygon> {
    //properties

    //methods
}
