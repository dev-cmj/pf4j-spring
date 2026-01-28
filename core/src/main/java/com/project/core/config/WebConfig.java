package com.project.core.config;

import org.laxture.sbp.internal.webmvc.PluginResourceResolver;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@DependsOn("pluginManager")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    @Lazy
    private PluginManager pluginManager;

    // 리졸버를 별도의 빈으로 등록하여 Spring이 내부 의존성을 주입하게 함
    @Bean
    public PluginResourceResolver pluginResourceResolver() {
        return new PluginResourceResolver();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(pluginResourceResolver()); // new 대신 빈(Bean) 호출
    }
}