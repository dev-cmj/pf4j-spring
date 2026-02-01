package com.project.core.plugin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;

public interface GatewayPluginFilter {

    int order();  // 체인 순서

    boolean supports(HttpServletRequest request);  // 적용 여부

    void preHandle(HttpServletRequest request, HttpHeaders proxyHeaders);

    void postHandle(HttpServletRequest request, HttpServletResponse response);
}
