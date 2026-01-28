package com.project.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.pf4j.PluginState;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginResponse {
    private String id;
    private String version;
    private String pluginClass;
    private String provider;
    private PluginState state;      // STARTED, STOPPED 등
    private String descriptorPath;  // plugin.properties 경로
    private boolean started;
}