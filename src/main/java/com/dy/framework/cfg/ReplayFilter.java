package com.dy.framework.cfg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dy.framework.util.MD5;
import com.dy.framework.util.RedisHelper;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:01:20
 */
@Slf4j
public class ReplayFilter implements Filter {

    @Autowired
    private RedisHelper redisHelper;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        String sign = params.get("sign");
        String name;
        String[] values;
        String valueStr = "";
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            name = iter.next();
            values = requestParams.get(name);
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
//            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }


        params.remove("sign");

        StringBuffer content = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }

        String mysign = MD5.encode(content.append("&key=aloestec").toString()).toUpperCase();
        log.info("签名 mysign={},sign={}", mysign,sign);
        if (!mysign.equals(sign)) { // 签名验证不通过，非法请求
            log.info("非法请求 IP={}, URL={}, params={}",
                    RequestLogFilter.getIpAddress(request), request.getRequestURI(),
                    content.delete(content.length() - 13, content.length()));
            return;
        }
        long timestamp = Long.parseLong(params.get("timestamp"));
        if (System.currentTimeMillis() - timestamp > 60 * 1000) { // 请求时间与当前时间间隔超过60s，非法请求
            log.info("非法请求 IP={}, URL={}, params={}",
                    RequestLogFilter.getIpAddress(request), request.getRequestURI(),
                    content.delete(content.length() - 13, content.length()));
            return;
        }
        String nonce = params.get("nonce");
        String mynonce = redisHelper.get("nonce:" + nonce);
        if (mynonce == null) { // redis中不存在当前请求的nonce，说明之前没有请求过，可以放行
            redisHelper.set("nonce:" + nonce, nonce, 60);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {}

}
