package com.example.typehandler;

import org.apache.ibatis.type.*;
import org.geotools.geojson.geom.GeometryJSON;
import org.locationtech.jts.geom.Geometry;


import java.io.IOException;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName AbstractGeometryTypeHandler
 * @Description: com.example.typehandler_将字符串转换为JTS对应的Geometry几何类型
 * @Auther: xiwd
 * @Date: 2022/4/6 - 04 - 06 - 11:51
 * @version: 1.0
 */
//用户自定义TypeHandler的注解
//@Configuration
@MappedJdbcTypes({JdbcType.OTHER})
@MappedTypes(Geometry.class)
public class AbstractGeometryTypeHandler<T extends Geometry> extends BaseTypeHandler<Geometry> {
    //methods
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Geometry parameter, JdbcType jdbcType) throws SQLException {
//        System.out.println(parameter);
        ps.setObject(i,parameter.toString());
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String srcSource = rs.getObject(columnName).toString();
//        System.out.println("111");
        GeometryJSON geometryJSON = new GeometryJSON();
        try {
            return geometryJSON.read(new StringReader(srcSource));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Geometry getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String srcSource = rs.getObject(columnIndex).toString();
//        System.out.println("222");
        GeometryJSON geometryJSON = new GeometryJSON();
        try {
            return geometryJSON.read(new StringReader(srcSource));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Geometry getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String srcSource = cs.getObject(columnIndex).toString();
//        System.out.println("333");
        GeometryJSON geometryJSON = new GeometryJSON();
        try {
            return geometryJSON.read(new StringReader(srcSource));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
