package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName BikeType
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 20:56
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BikeType implements Serializable {
    private static final long serialVersionUID = -160017941657105692L;
    //properties
    private Integer id;//ID序号
    private String type;//单车类型
    private String description;//描述信息
    private BigDecimal price;//单车成本价格/元
    private Float limits;//使用年限
    private byte[] photo;//单车样例照片
    //methods

}
