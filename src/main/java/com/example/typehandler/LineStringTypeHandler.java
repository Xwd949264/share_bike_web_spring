package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.LineString;

/**
 * @ClassName LineStringTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:28
 * @version: 1.0
 */
//The annotation that specify java types to map
@MappedTypes(LineString.class)
public class LineStringTypeHandler extends AbstractGeometryTypeHandler<LineString> {
    //properties

    //methods
}
