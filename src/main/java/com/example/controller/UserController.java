package com.example.controller;

import cn.hutool.core.util.StrUtil;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;


/**
 * @ClassName UserController
 * @Description: com.example.controller
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:30
 * @version: 1.0
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
    //properties
    @Autowired
    private UserService userService;

    //methods
    /**
     * 用户注册
     * @param username 账户
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Result register(@RequestParam(value = "username")String username,
                           @RequestParam(value = "password")String password){
        try {
            if (StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
                return ResultUtil.error(0,"用户名或者密码为空");
            }
            return userService.register(username, password);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"未知异常");
        }
    }

    /**
     * 用户登录
     * @param username 账户
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    @ResponseBody
    public Result login(@RequestParam(value = "username")String username,
                           @RequestParam(value = "password")String password){
        try {
            if (StrUtil.isEmpty(username)||StrUtil.isEmpty(password)){
                return ResultUtil.error(0,"用户名或者密码为空");
            }
            return userService.login(username, password);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error(-1,"未知异常");
        }
    }

    /**
     * 更新用户头像
     * @param username 账户
     * @param photo 密码
     * @return
     */
    @RequestMapping(value = "/update/photo",method = RequestMethod.POST)
    @ResponseBody
    public Result updatePhoto(@RequestParam(value = "username") String username,
                              @RequestParam(value = "photo") MultipartFile photo) throws IOException {
        if (StrUtil.isEmpty(username)||photo==null)
            return ResultUtil.error(0,"信息上报不完整!");
        //更新用户头像
        System.out.println(username);
        System.out.println(photo.getBytes());
        return userService.updatePhotoByUsername(username, photo);
    }

    /**
     * 获取用户头像
     * @param username 账户
     * @return
     */
    @RequestMapping(value = "/get/photo",method = RequestMethod.GET)
    @ResponseBody
    public Result getPhoto(@RequestParam(value = "username") String username, HttpServletResponse response) {
        if (StrUtil.isEmpty(username))
            return ResultUtil.error(0,"头像获取失败!");
        //获取用户头像
        return userService.getUserPhotoByUsername(username,response);
    }

    /**
     * 根据用户名更新用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update/user",method = RequestMethod.POST)
    @ResponseBody
    public Result<User> updateUserInfoByUname(User user){
        System.out.println(user);
        if (user!=null&&(!StrUtil.isEmpty(user.getAccount()))){
            return userService.updateUserInfoByUname(user.getAccount(),user);
        }else {
            return ResultUtil.error(0,"必选参数为空");
        }
    }

    /**
     * 根据用户名查找用户信息
     * @param username
     * @return
     */
    @RequestMapping(value = "/get/user",method = RequestMethod.GET)
    @ResponseBody
    public Result<User> getUserInfoByUname(@RequestParam(value = "username") String username){
        if (StrUtil.isEmpty(username))
            return ResultUtil.error(0,"查询参数为空!");
        return userService.getUserInfoByUname(username);
    }

}
