package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.MultiLineString;

/**
 * @ClassName MultiLineStringTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:30
 * @version: 1.0
 */
@MappedTypes(MultiLineString.class)
public class MultiLineStringTypeHandler extends AbstractGeometryTypeHandler<MultiLineString> {
    //properties

    //setter

    //getter

    //constructors

    //methods
}
