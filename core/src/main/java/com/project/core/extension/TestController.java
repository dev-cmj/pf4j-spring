package com.project.core.extension;

import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private PluginManager pluginManager;  // @Autowired 추가!

    @GetMapping("/plugin-list")
    public List<String> listPlugins() {
        List<PluginWrapper> plugins = pluginManager.getPlugins();
        return plugins != null ?
                plugins.stream().map(PluginWrapper::getPluginId).toList() :
                List.of("No plugins");
    }

    @GetMapping("/extensions")
    public List<String> listExtensions() {
        List<PluginController> extensions = pluginManager.getExtensions(PluginController.class);
        return extensions.stream()
                .map(c -> c.getClass().getSimpleName())
                .toList();
    }
}

