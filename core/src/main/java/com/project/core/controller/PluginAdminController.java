package com.project.core.controller;

import com.project.core.dto.PluginResponse;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/plugins")
public class PluginAdminController {

    @Autowired
    private PluginManager pluginManager;

    @GetMapping
    public String adminPage(Model model) {
        var plugins = pluginManager.getPlugins().stream()
                .map(this::mapToPluginResponse) // 기존 메서드 호출
                .toList();

        model.addAttribute("plugins", plugins);
        return "admin/plugins";
    }

    private PluginResponse mapToPluginResponse(PluginWrapper plugin) {
        PluginState state = plugin.getPluginState();  // PF4J PluginWrapper 표준 메서드
        return PluginResponse.builder()
                .id(plugin.getPluginId())
                .version(plugin.getDescriptor().getVersion())
                .pluginClass(plugin.getDescriptor().getPluginClass())
                .provider(plugin.getDescriptor().getProvider())
                .state(state)
                .descriptorPath(plugin.getPluginPath() + "/plugin.properties")  // Properties 기반 표준 경로
                .started(PluginState.STARTED.equals(state))
                .build();
    }

}