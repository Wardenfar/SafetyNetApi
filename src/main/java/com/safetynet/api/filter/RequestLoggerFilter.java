package com.safetynet.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class RequestLoggerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("Request {} {}", req.getMethod(), req.getRequestURI());
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error : {}", e.getMessage(), e);
            return;
        }
        log.info("Response {} ({})", res.getStatus(), res.getContentType());
    }
}
