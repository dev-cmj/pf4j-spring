package com.project.core.controller;

import com.project.core.interceptor.TenantContextHolder;
import com.project.core.plugin.GatewayPluginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/api/**")
public class GatewayController {

    @Autowired
    private RestClient restClient;

    @Autowired
    private List<GatewayPluginFilter> pluginFilters;  // SBP가 플러그인 빈으로 주입해줄 것

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> proxy(HttpServletRequest request) {
        String path = request.getRequestURI().replace("/api/", "");
        String tenant = TenantContextHolder.getTenant();
        String targetUrl = "http://" + tenant + "-service/" + path;

        HttpHeaders headers = copyHeaders(request);

        // 1) 플러그인 preHandle 체인
        pluginFilters.stream()
                .sorted(Comparator.comparingInt(GatewayPluginFilter::order))
                .filter(f -> f.supports(request))
                .forEach(f -> f.preHandle(request, headers));

        // 2) 실제 백엔드 호출
        ResponseEntity<String> response = restClient.method(HttpMethod.valueOf(request.getMethod()))
                .uri(targetUrl)
                .headers(h -> h.addAll(headers))
                .retrieve()
                .toEntity(String.class);

        // 3) 플러그인 postHandle 체인
        pluginFilters.stream()
                .sorted(Comparator.comparingInt(GatewayPluginFilter::order))
                .filter(f -> f.supports(request))
                .forEach(f -> f.postHandle(request, null));  // 필요시 ResponseEntity 넘기도록 확장

        return response;
    }

    private HttpHeaders copyHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();

        // 1. 모든 헤더 순회 복사
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);

            // 2. 게이트웨이에서 제거할 헤더
            if (isInternalHeader(headerName)) {
                continue;
            }

            headers.add(headerName, headerValue);
        }

        // 3. TenantContext 추가 (필수)
        String tenant = TenantContextHolder.getTenant();
        if (tenant != null) {
            headers.add("X-Tenant-ID", tenant);
        }

        return headers;
    }

    private boolean isInternalHeader(String headerName) {
        // 게이트웨이 내부 헤더 제거
        return "Host".equalsIgnoreCase(headerName) ||
                "X-Tenant-ID".equalsIgnoreCase(headerName) ||  // 중복 방지
                "X-Forwarded-For".equalsIgnoreCase(headerName) ||
                "X-Real-IP".equalsIgnoreCase(headerName);
    }




}
