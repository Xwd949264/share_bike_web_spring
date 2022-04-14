package com.example.mapper;

import com.example.pojo.Result;
import com.example.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName UserMapper
 * @Description: com.example.mapper
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:41
 * @version: 1.0
 */
public interface UserMapper {
    //methods

    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    public User selectUserByUname(@Param(value = "username") String username);

    /**
     * 用户注册功能
     * @param username
     * @param password
     * @return
     */
    public Integer insertUser(@Param(value = "username") String username,
                            @Param(value = "password") String password);

    /**
     * 根据用户名和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    public User selectUserByUnameAndPswd(@Param(value = "username") String username,
                                         @Param(value = "password") String password);

    /**
     * 根据用户名更新用户头像
     * @param username
     * @param bytes
     * @return
     */
    public Integer updatePhotoByUsername(@Param(value = "username") String username,
                                         @Param(value = "photo") byte[] bytes);

    /**
     * 根据用户名更新用户信息
     * @param username
     * @param user
     * @return
     */
    public Integer updateUserInfoByUname(@Param(value = "username") String username,
                                         @Param(value = "user") User user);
}
