package com.safetynet.api.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class RequestLoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        Logger.info("Request {} {}", req.getMethod(), req.getRequestURI());
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            Logger.error("Error : {}", e.getMessage(), e);
            return;
        }
        Logger.info("Response {} ({})", res.getStatus(), res.getContentType());
    }
}
