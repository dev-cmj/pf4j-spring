package com.project.core.extension;

import org.pf4j.JarPluginManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Paths;

@Configuration
public class PluginManagerConfig {

    @Value("${pf4j.plugins-dir:plugins}")
    private String pluginsDir;

    @Bean
    public JarPluginManager pluginManager() {
        return new JarPluginManager(Paths.get(pluginsDir));
    }
}
