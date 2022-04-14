package com.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -5795691457907462318L;
    private String account;//账户名[长度小于36]
    private String password;//密码
    private String name;//姓名
    private Integer age;//年龄
    private String gender;//性别
    private String tel;//手机号
    private String email;//邮箱
    private String address;//通讯地址
    private Integer role;//用户身份[0-普通用户;1-管理员]
    private String hobby;//兴趣
    private String signature;//个性签名
    private byte[] photo;//照片

}