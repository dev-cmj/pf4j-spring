package com.project.core.controller;

import com.project.core.dto.PluginListResponse;
import com.project.core.dto.PluginResponse;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


@RequestMapping("/v1/plugins")
@RestController
public class PluginController {

    @Autowired
    private PluginManager pluginManager;

    @GetMapping
    public ResponseEntity<PluginListResponse> listPlugins() {
        List<PluginResponse> plugins = pluginManager.getPlugins().stream()
                .map(this::mapToPluginResponse)
                .toList();

        var response = PluginListResponse.builder()
                .plugins(plugins)
                .totalCount(plugins.size())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{pluginId}")
    public ResponseEntity<PluginResponse> getPluginInfo(@PathVariable String pluginId) {
        PluginWrapper plugin = pluginManager.getPlugin(pluginId);
        if (plugin == null) {
            return ResponseEntity.notFound().build();
        }

        var response = mapToPluginResponse(plugin);
        return ResponseEntity.ok(response);
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

    @PostMapping("/{pluginId}/start")
    public ResponseEntity<Void> startPlugin(@PathVariable String pluginId) {
        // 플러그인을 시작하고 상태를 반환
        PluginState state = pluginManager.startPlugin(pluginId);
        return state == PluginState.STARTED ? ResponseEntity.ok().build() : ResponseEntity.internalServerError().build();
    }

    @PostMapping("/{pluginId}/stop")
    public ResponseEntity<Void> stopPlugin(@PathVariable String pluginId) {
        pluginManager.stopPlugin(pluginId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPlugin(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".jar")) {
            return ResponseEntity.badRequest().body("올바른 JAR 파일을 선택해주세요.");
        }

        try {
            // 1. 설정된 plugins-root 경로 가져오기
            Path pluginsRoot = Path.of(System.getProperty("user.dir"), "core/plugins");
            if (!Files.exists(pluginsRoot)) Files.createDirectories(pluginsRoot);

            // 2. 파일 저장
            Path targetPath = pluginsRoot.resolve(file.getOriginalFilename());
            file.transferTo(targetPath);

            // 3. PF4J에 플러그인 로드 및 시작
            // sbp의 PluginManager를 통해 새 파일을 인식시킵니다.
            String pluginId = pluginManager.loadPlugin(targetPath);
            if (pluginId != null) {
                pluginManager.startPlugin(pluginId);
                return ResponseEntity.ok("플러그인 업로드 및 시작 성공: " + pluginId);
            } else {
                return ResponseEntity.internalServerError().body("플러그인 로드 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }


}
