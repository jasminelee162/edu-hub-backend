package com.project.admin.controller.login;

import com.alibaba.fastjson2.JSONObject;
import com.project.common.annotation.Log;
import com.project.common.constant.Constants;
import com.project.common.domain.Result;
import com.project.common.enums.BusinessType;
import com.project.common.utils.JwtUtil;
import com.project.common.utils.PasswordUtils;
import com.project.common.utils.UserAgentUtil;
import com.project.framework.event.LoginLogEvent;
import com.project.framework.utils.RedisUtils;
import com.project.framework.utils.RequestUtils;
import com.project.framework.utils.ShiroUtils;
import com.project.system.domain.LoginLog;
import com.project.system.domain.User;
import com.project.system.service.LoginLogService;
import com.project.system.service.MenuService;
import com.project.system.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: 登陆
 * @date 2023/9/8 9:04
 */
@Controller
@ResponseBody
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLogService loginLogService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping()
    public Result login(HttpServletRequest request,@RequestBody JSONObject jsonObject) {
        String ipAddr = RequestUtils.getRemoteHost(request);
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getLoginAccount,username);
        User user = userService.getOne(query);
        if (user == null) {
            saveLoginLog(request,"用户名不存在",username,ipAddr,1);
            return Result.fail("用户名不存在！");
        }
        //比较加密后得密码
        boolean decrypt = PasswordUtils.decrypt(password, user.getPassword() + "$" + user.getSalt());
        if (!decrypt) {
            saveLoginLog(request,"用户名或密码错误",username,ipAddr,1);
            return Result.fail("用户名或密码错误！");
        }
        if (user.getStatus() == 1) {
            saveLoginLog(request,"用户被禁用",username,ipAddr,1);
            return Result.fail("用户被禁用！");
        }
        //密码正确生成token返回
        String token = JwtUtil.sign(user.getId(), password);
        JSONObject json = new JSONObject();
        json.put("token", token);
        saveLoginLog(request,"登录成功",username,ipAddr,0);
        return Result.success(json);
    }

    @PostMapping("register")
    public Result register(@RequestBody User user) {
        boolean account = checkAccount(user);
        boolean email = checkEmail(user.getEmail());
        if (!account) {
            return Result.fail("登录账号已存在不可重复！");
        }
        if (!email) {
            return Result.fail("邮箱已被注册不可重复！");
        }
        //密码加盐加密
        String encrypt = PasswordUtils.encrypt(user.getPassword());
        String[] split = encrypt.split("\\$");
        user.setPassword(split[0]);
        user.setSalt(split[1]);
        user.setAvatar("/img/avatar.jpg");
        user.setPwdUpdateDate(new Date());
        user.setChecked(1);
        boolean save = userService.save(user);
        if (save) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /** 校验用户 */
    public boolean checkAccount(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getLoginAccount, user.getLoginAccount())
                .eq(User::getUserType, user.getUserType());
        int count = userService.count(queryWrapper);
        return count <= 0;
    }

    public boolean checkEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getEmail, email);
        int count = userService.count(queryWrapper);
        return count <= 0;
    }



    @Log(name = "登出", type = BusinessType.OTHER)
    @GetMapping("logout")
    public Result logout() {
        User user = ShiroUtils.getUserInfo();
        redisUtils.remove(Constants.PREFIX_USER_TOKEN + user.getId());
        return Result.success();
    }

    @Log(name = "验证密码", type = BusinessType.OTHER)
    @GetMapping("verPassword")
    public Result verPassword(HttpServletRequest request,@RequestParam("password") String password) {
        User user = ShiroUtils.getUserInfo();
        User apeUser = userService.getById(user.getId());
        String ipAddr = RequestUtils.getRemoteHost(request);
        if (apeUser.getStatus() == 1) {
            saveLoginLog(request,"用户被禁用",apeUser.getLoginAccount(),ipAddr,1);
            return Result.fail("用户被禁用！");
        }
        boolean decrypt = PasswordUtils.decrypt(password, apeUser.getPassword() + "$" + apeUser.getSalt());
        if (!decrypt) {
            saveLoginLog(request,"验证密码错误",apeUser.getLoginAccount(),ipAddr,1);
            return Result.fail("用户名或密码错误！");
        }
        saveLoginLog(request,"验证成功",apeUser.getLoginAccount(),ipAddr,0);
        return Result.success();
    }


    public void saveLoginLog(HttpServletRequest request,String msg,String username,String ipAddr,Integer state) {
        String agent = request.getHeader("User-Agent");
        String userAgent = UserAgentUtil.getUserAgent(agent);
        String browser = UserAgentUtil.judgeBrowser(userAgent);
        LoginLog loginLog = new LoginLog();
        loginLog.setUserName(username);
        loginLog.setLoginIp(ipAddr);
        loginLog.setBrowser(browser);
        loginLog.setOs(userAgent);
        loginLog.setStatus(state);
        loginLog.setLoginTime(new Date());
        loginLog.setMsg(msg);
        eventPublisher.publishEvent(new LoginLogEvent(loginLog));
    }

}
