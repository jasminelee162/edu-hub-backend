package com.project.framework.filter;

import com.alibaba.fastjson2.JSONObject;
import com.project.common.utils.JwtUtil;
import com.project.framework.custom.JwtToken;
import com.project.framework.utils.RequestUtils;
import lombok.SneakyThrows;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author shaozhujie
 * @version 1.0
 * @description: jwt过滤器
 * @date 2023/8/11 9:59
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
    * @description: 执行登录认证
    * @param: request
    	response
    	mappedValue
    * @return:
    * @author shaozhujie
    * 
    */
    private static final List<String> EXCLUDED_URLS = Arrays.asList(
            "/ai","/suggestion","/sandbox/execute","/sandbox/questions","/sandbox/description"
            ,"/sandbox/title","/user/unread","/template/show","/document/create","/template/content"
           ,"/email/sendCode","/email/login"
    );
    @SneakyThrows
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        // 如果是放行路径，直接通过
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();
        if (EXCLUDED_URLS.stream().anyMatch(path::startsWith)) {
            return true;
        }
        try {
            executeLogin(request, response);
        } catch (IncorrectCredentialsException e) {
            JSONObject json = new JSONObject();
            json.put("code",1011);
            json.put("message",e.getMessage());
            json.put("timeStamp",System.currentTimeMillis());
            RequestUtils.returnJson(httpServletResponse,json.toJSONString());
            return false;
        } catch (LockedAccountException e) {
            JSONObject json = new JSONObject();
            json.put("code",1009);
            json.put("message",e.getMessage());
            json.put("timeStamp",System.currentTimeMillis());
            RequestUtils.returnJson(httpServletResponse,json.toJSONString());
            return false;
        } catch (UnknownAccountException e) {
            JSONObject json = new JSONObject();
            json.put("code",1008);
            json.put("message",e.getMessage());
            json.put("timeStamp",System.currentTimeMillis());
            RequestUtils.returnJson(httpServletResponse,json.toJSONString());
            return false;
        } catch (AuthenticationException e) {
            JSONObject json = new JSONObject();
            json.put("code",1006);
            json.put("message",e.getMessage());
            json.put("timeStamp",System.currentTimeMillis());
            RequestUtils.returnJson(httpServletResponse,json.toJSONString());
            return false;
        }
        return true;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = JwtUtil.getTokenByRequest(httpServletRequest);
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

}
