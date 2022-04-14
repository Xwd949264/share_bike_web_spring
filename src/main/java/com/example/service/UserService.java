package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.pojo.Result;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName UserService
 * @Description: com.example.service
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:00
 * @version: 1.0
 */
public interface UserService {
    //methods

    /**
     * 用户注册功能
     * @param username
     * @param password
     * @return
     */
    public Result register(String username, String password);


    /**
     * 用户登录
     * @param username 账户
     * @param password 密码
     * @return
     */
    public Result login(String username, String password);

    /**
     * 更新用户头像
     * @param username
     * @param photo
     * @return
     */
    public Result updatePhotoByUsername(String username, MultipartFile photo);

    /**
     * 根据用户名获取用户头像
     * @param username
     * @param response
     * @return
     */
    public Result getUserPhotoByUsername(String username, HttpServletResponse response);

    /**
     * 根据用户名修改用户信息
     * @param username
     * @param user
     * @return
     */
    public abstract Result updateUserInfoByUname(String username,User user);

    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    public Result<User> getUserInfoByUname(String username);

}
