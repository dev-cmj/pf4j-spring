package com.project.core.controller;

import org.laxture.sbp.SpringBootPluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @Autowired
    private SpringBootPluginManager pluginManager;  // null 체크

    @GetMapping("/debug/sbp")
    public Map<String, Object> debugSbp() {
        Map<String, Object> info = new HashMap<>();
        info.put("pluginManager", pluginManager != null ? "OK" : "NULL ← 문제");
        info.put("pluginsCount", pluginManager != null ? pluginManager.getPlugins().size() : 0);
        return info;
    }
}
