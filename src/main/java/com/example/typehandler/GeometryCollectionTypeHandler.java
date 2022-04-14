package com.example.typehandler;

import org.apache.ibatis.type.MappedTypes;
import org.locationtech.jts.geom.GeometryCollection;

/**
 * @ClassName GeometryCollectionTypeHandler
 * @Description: com.example.config.typehandler
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 13:29
 * @version: 1.0
 */
@MappedTypes(GeometryCollection.class)
public class GeometryCollectionTypeHandler extends AbstractGeometryTypeHandler<GeometryCollection> {
    //properties

    //setter

    //getter

    //constructors

    //methods
}
