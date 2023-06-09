package cn.ljcljc.controller;

import cn.hutool.crypto.SecureUtil;
import cn.ljcljc.mapper.UserMapper;
import cn.ljcljc.pojo.Result;
import cn.ljcljc.pojo.User;
import cn.ljcljc.service.UserService;
import cn.ljcljc.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeString.substring;


/**
 * 用户控制层
 * @author 李锦成
 * @version 1.0(2023.06)
 */


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;


    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 状态码和用户信息
     */
    @RequestMapping("/login")
    public Result login(String username, String password) {

        User e = userMapper.login(username, password);

        if(e != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("uid", e.getUserId());
            claims.put("username", e.getUserName());
            claims.put("name", e.getName());

            String jwt = JwtUtils.generateJwt(claims);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("token", jwt);
            userInfo.put("code", e.getCode());

            System.out.println(e.getLove_id());

            if(e.getLove_id() == null) {
                return Result.success(userInfo);
            }
            else {
                return Result.success(201, userInfo);
            }
        }
        else {
            return Result.error(400, "账号或密码错误", null);
        }
    }

    /**
     * 注册
     * @param username 用户名
     * @param name 姓名
     * @param password 密码
     * @return 状态码
     */
    @RequestMapping("/register")
    public Result register(String username, String name, String password) {
        if(!username.equals("") && !name.equals("") && !password.equals("")) {
            User e = userMapper.getUserDataByUserName(username);
            System.out.println(e);
            if(e == null) {

                // 生成唯一code
                long currentTime = new Date().getTime();
                double randomNum = Math.random()*1000;

                String originalCode = String.valueOf(currentTime) + String.valueOf(randomNum);
                String encryptedCode = SecureUtil.md5(originalCode);
                String code = substring(encryptedCode, 1, 11);

                userMapper.register(username, password, name, code);
            }
            else {
                return Result.error(100, "该用户名已存在，请重新输入用户名", null);
            }
            return Result.success();
        }
        else return Result.error(100,"信息不能为空", null);
    }

    /**
     * 绑定对象
     * @param code 对象的绑定code
     * @param token 获取Header中存放的token，用于身份校验
     * @return 状态码
     */
    @RequestMapping("/bind")
    public Result bind(String code, @RequestHeader("token")String token) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));

        if(currentUser.getLove_id() == null) {
            int status = userService.bindLoverService(code, currentUser);
            if(status == 1) {
                return Result.error(100, "对方已经绑定过啦", null);
            }
            else if(status == 2) {
                return Result.error(100, "口令不存在", null);
            }
            else if(status == 3) {
                return Result.error(100, "不能绑定自己哦", null);
            }
        }
        else {
            return Result.error(100, "你已经绑定过啦", null);
        }
        return Result.success();
    }

    /**
     * 检测是否绑定对象
     * @param token 获取Header中存放的token，用于身份校验
     * @return 状态码和绑定状态
     */
    @RequestMapping("/checkbind")
    public Result checkBind(@RequestHeader("token")String token) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        if (currentUser.getLove_id() != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("isBind", true);
            return Result.success(response);
        }
        return Result.success();
    }

    /**
     * 修改密码
     * @param token 获取Header中存放的token，用于身份校验
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 状态码和绑定状态
     */
    @RequestMapping("/changePassword")
    public Result checkBind(@RequestHeader("token")String token, String oldPassword, String newPassword) {
        User currentUser = userMapper.getUserDataByUserId(JwtUtils.verifyUser(token));
        User testUser = userMapper.login(currentUser.getUserName(), oldPassword);
        if(testUser == null) return Result.error(100, "原密码错误", null);

        userMapper.updatePassword(newPassword, currentUser.getUserId());
        return Result.success();
    }


}
