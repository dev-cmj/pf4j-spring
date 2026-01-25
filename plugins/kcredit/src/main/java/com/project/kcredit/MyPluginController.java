package com.project.kcredit;

import com.project.core.extension.PluginController;
import com.project.core.extension.PluginService;
import lombok.RequiredArgsConstructor;
import org.pf4j.Extension;
import org.pf4j.ExtensionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Extension  // pf4j 확장
@RestController
@RequestMapping("/plugin/kcredit")
public class MyPluginController implements PluginController {

    public MyPluginController() {  // ← 기본 생성자 필수!
        // 빈 생성자
    }

    @GetMapping("/credit-check")
    public ResponseEntity<String> creditCheck() {
        return ResponseEntity.ok("Kcredit Plugin: Credit check successful!");
    }
}
