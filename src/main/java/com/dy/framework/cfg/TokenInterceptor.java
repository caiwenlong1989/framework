package com.dy.framework.cfg;

import com.alibaba.fastjson.JSON;
import com.dy.framework.util.JWT;
import com.dy.framework.util.ResultUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:01:27
 */
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头Authorization
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer") || authorization.length() < 7) {
            return response(response, ResultUtil.Enums.AUTHORIZATION_NULL.getCode());
        }

        // 从获取请求头Authorization中获取accessToken
        String accessToken = authorization.substring(7);
        try {
            // 使用JWT解析Token
            String id = JWT.parse(accessToken);
            request.setAttribute(Constants.USER_ID, Integer.valueOf(id));
            return true;
        } catch (MalformedJwtException | SignatureException e) {
            log.error("！！！[IP={}]非法入侵！！！", RequestLogFilter.getIpAddress(request));
            return response(response, ResultUtil.Enums.ACCESS_TOKEN_ERROR.getCode());
        } catch (ExpiredJwtException e) {
            if ("/user/refreshToken".equals(request.getServletPath())) {
                String refreshToken = request.getParameter("refreshToken");
                try {
                    // 使用JWT解析Token
                    String id = JWT.parse(refreshToken);
                    request.setAttribute(Constants.USER_ID, Integer.valueOf(id));
                    return true;
                } catch (MalformedJwtException | SignatureException e2) {
                    log.error("！！！[IP={}]非法入侵！！！", RequestLogFilter.getIpAddress(request));
                    return response(response, ResultUtil.Enums.REFRESH_TOKEN_ERROR.getCode());
                } catch (ExpiredJwtException e2) {
                    return response(response, ResultUtil.Enums.REFRESH_TOKEN_EXPIRE.getCode());
                }
            }
            return response(response, ResultUtil.Enums.ACCESS_TOKEN_EXPIRE.getCode());
        }
    }

    private boolean response(HttpServletResponse response, Integer code) throws IOException {
        response.setContentType(ContentType.APPLICATION_JSON.toString());
        String json = JSON.toJSONString(ResultUtil.fail(code));
        response.getWriter().print(json);
        return false;
    }

}
