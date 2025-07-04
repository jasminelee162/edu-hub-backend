package com.project.admin.controller.email;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.common.domain.Result;
import com.project.common.utils.JwtUtil;
import com.project.common.utils.UserAgentUtil;
import com.project.framework.event.LoginLogEvent;
import com.project.framework.utils.RedisUtils;
import com.project.framework.utils.RequestUtils;
import com.project.framework.utils.ValidateCodeUtils;
import com.project.system.domain.LoginLog;
import com.project.system.domain.User;
import com.project.system.service.EmailService;
import com.project.system.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// AuthController.java
@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private UserService userService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    // 发送验证码接口
    @PostMapping("/sendCode")
    public Result sendVerificationCode(@RequestBody EmailRequest request) {
        String email = request.getEmail();
        // 生成6位验证码
        String code = String.valueOf(ValidateCodeUtils.generateValidateCode(6));
        // 发送邮件
        emailService.sendVerificationCode(email, code);
        // 缓存验证码
       redisUtils.set("emailCode", code,300L);
        return Result.success("验证码已发送");
    }

    // 验证码登录接口
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String email = loginRequest.getEmail();
        String code = loginRequest.getCode();
        String ipAddr = RequestUtils.getRemoteHost(request);

        QueryWrapper<User> query = new QueryWrapper<>();
        query.lambda().eq(User::getEmail,email);
        User user = userService.getOne(query);

        if (user == null) {
            return Result.fail("邮箱或验证码错误");
        }

        String correctCode = redisUtils.get("emailCode");
        // 验证验证码
        if (!code.equals(correctCode)) {
            return Result.fail("邮箱或验证码错误");
        }

        //密码正确生成token返回
        String token = JwtUtil.sign(user.getId(), email);
        JSONObject json = new JSONObject();
        json.put("token", token);
        saveLoginLog(request,"登录成功",user.getUserName(),ipAddr,0);
        return Result.success(json);

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

// 辅助类
@Data
class EmailRequest {
    private String email;
}

@Data
class LoginRequest {
    private String email;
    private String code;
}