package com.aloestec.framework.cfg;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author caiwl
 * 2018-06-08 17:01:24
 */
@Slf4j
public class RequestLogFilter implements Filter {

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        StringBuffer params = new StringBuffer();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.append("&").append(name).append("=").append(valueStr);
        }
        
        log.info("Before request [uri={} {}, client={}, params={}]",
                request.getMethod(), request.getRequestURI(), getIpAddress(request), params.length() > 0 ? params.substring(1) : "");
        
        filterChain.doFilter(request, response);
        
        log.info("After request [uri={} {}, client={}, status={}]",
                request.getMethod(), request.getRequestURI(), getIpAddress(request), response.getStatus());
        
    }
    
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (invalid(ip)) {
            ip = request.getHeader("Proxy-Client-IP");

            if (invalid(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");

            }
            if (invalid(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");

            }
            if (invalid(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (invalid(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
    
    private static boolean invalid(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {}

}
