package com.safetynet.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class RequestLoggerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("REQ {} {}", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
        log.info("RES {} ({})", res.getStatus(), res.getContentType());
    }
}