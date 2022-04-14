package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName BikeStatus
 * @Description: com.example.pojo
 * @Auther: xiwd
 * @Date: 2022/4/10 - 04 - 10 - 20:59
 * @version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BikeStatus {
    private Integer status;//状态值
    private String description;//状态描述
}
