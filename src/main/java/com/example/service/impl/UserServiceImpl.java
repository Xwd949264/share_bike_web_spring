package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.example.mapper.UserMapper;
import com.example.pojo.Result;
import com.example.pojo.User;
import com.example.service.UserService;
import com.example.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserServiceImpl
 * @Description: com.example.service.impl
 * @Auther: xiwd
 * @Date: 2022/4/9 - 04 - 09 - 9:00
 * @version: 1.0
 */
@Service
public class UserServiceImpl implements UserService {
    //properties
    @Autowired
    private UserMapper userMapper;

    //methods
    @Override
    public Result register(String username, String password) {
        //查询用户是否存在
        User user = userMapper.selectUserByUname(username);
        if (user==null){
            //md5密码加密
            String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
            Integer rows = userMapper.insertUser(username, md5Password);
            if (rows>0){
                return ResultUtil.success(1,"注册成功");
            }
            return ResultUtil.error(0,"注册失败");
        }else {
            return  ResultUtil.error(-1,"当前用户名已被注册");
        }
    }

    @Override
    public Result login(String username, String password) {
        //密码加密
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        //查询用户是否存在
        User user = userMapper.selectUserByUnameAndPswd(username, md5Password);
        if (user==null)
            return ResultUtil.success(0,"用户名或者密码错误");
        else
            return ResultUtil.success(1,"登录成功");
    }

    @Override
    public Result updatePhotoByUsername(String username, MultipartFile photo) {
        try {
            Integer rows = userMapper.updatePhotoByUsername(username, photo.getBytes());
            if (rows>0)
                return ResultUtil.success(1,"用户照片已更新!");
            return ResultUtil.error(0,"用户照片更新失败!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.success(-1,"系统异常");
        }
    }

    @Override
    public Result getUserPhotoByUsername(String username, HttpServletResponse response) {
        User user = userMapper.selectUserByUname(username);
        //解析用户头像
        if (user!=null&&user.getPhoto()!=null){
            byte[] photo = user.getPhoto();
            ByteArrayInputStream inputStream = null;
            ServletOutputStream outputStream = null;
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                inputStream = new ByteArrayInputStream(photo);
                outputStream = response.getOutputStream();
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return ResultUtil.error(0,"头像获取失败!");
            } finally {
                if (outputStream!=null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream!=null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ResultUtil.success(1,"获取头像成功");
        }
        return ResultUtil.error(0,"头像获取失败!");
    }

    @Override
    public Result updateUserInfoByUname(String username, User user) {
        try {
            if (StrUtil.isEmpty(username)){
                return ResultUtil.error(0,"用户名不能为空");
            }
            //判断是否包含密码字段
            if (!StrUtil.isEmpty(user.getPassword())){
                //密码不为空
                //先加密
                String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
                //重新设置属性
                user.setPassword(md5Password);
            }
            //通过mapper更新用户信息
            Integer rows = userMapper.updateUserInfoByUname(username, user);
            if (rows>0){
                return ResultUtil.success(1,"更新成功");
            }
            return ResultUtil.error(0,"更新失败");
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.success(-1,"系统异常");
        }
    }


    @Override
    public Result<User> getUserInfoByUname(String username) {
       try {
           User user = userMapper.selectUserByUname(username);
           if (user!=null)
               return ResultUtil.success(1,"获取用户信息成功",user);
           else
               return ResultUtil.error(0,"获取用户信息失败");
       }catch (Exception e){
           e.printStackTrace();
           return ResultUtil.success(-1,"系统异常");
       }
    }

}
