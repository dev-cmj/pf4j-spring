package com.project.core.extension;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pf4j.PluginManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RestController
@RequestMapping("/plugin")
public class PluginControllerProxy {

    private final Map<String, Object> controllers = new ConcurrentHashMap<>();
    private final PluginManager pluginManager;
    private final ConfigurableListableBeanFactory beanFactory;

    public PluginControllerProxy(PluginManager pluginManager,
                                 ConfigurableListableBeanFactory beanFactory) {
        this.pluginManager = pluginManager;
        this.beanFactory = beanFactory;
        initPlugins();
    }

    private void initPlugins() {
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        List<PluginController> controllers = pluginManager.getExtensions(PluginController.class);
        for (PluginController controller : controllers) {
            String pluginId = controller.getClass().getSimpleName()
                    .replace("Controller", "").toLowerCase();
            this.controllers.put(pluginId, controller);

            // Spring 빈으로도 등록
            beanFactory.registerSingleton("plugin_" + pluginId, controller);
        }
        System.out.println("Loaded " + controllers.size() + " plugin controllers: " + this.controllers.keySet());
    }

    @RequestMapping("/{pluginId}/**")
    public ResponseEntity<String> executePlugin(
            @PathVariable String pluginId,
            HttpServletRequest request,
            HttpServletResponse response) {

        Object controller = controllers.get(pluginId);
        if (controller == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            String pathInfo = request.getPathInfo();
            String methodName = extractMethodName(pathInfo);

            // 플러그인 메서드 직접 호출 (간단 구현)
            Method method = controller.getClass().getMethod(methodName);
            Object result = method.invoke(controller);

            return ResponseEntity.ok((String) result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    private String extractMethodName(String pathInfo) {
        // /kcredit/credit-check → creditCheck
        if (pathInfo == null || pathInfo.equals("/")) return "index";
        return pathInfo.substring(1).replace("-", "").replaceAll("/", "");
    }

    @PreDestroy
    public void shutdown() {
        pluginManager.stopPlugins();
    }
}
