package com.project.core.config;

import com.project.core.interceptor.TenantContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                // 1. Consumer<HttpHeaders> 방식 (모든 버전 호환)
                .defaultHeaders(headers -> {
                    headers.add("X-Gateway-Version", "1.0");
                    // Tenant는 request 시점에 동적 추가 (ThreadLocal)
                })

                // 2. requestInterceptor로 TenantContext 추가 (권장)
                .requestInterceptor((request, body, execution) -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("X-Tenant-ID", TenantContextHolder.getTenant());
                    request.getHeaders().addAll(headers);
                    return execution.execute(request, body);
                })
                .build();
    }
}
