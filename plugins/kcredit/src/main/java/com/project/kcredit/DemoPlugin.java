package com.project.kcredit;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SbpThymeleafConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginWrapper;

public class DemoPlugin extends SpringBootPlugin {

    public DemoPlugin(PluginWrapper wrapper) {
        super(wrapper, new SbpThymeleafConfigurer());
    }

    @Override
    protected SpringBootstrap createSpringBootstrap() {
        return new SpringBootstrap(this, DemoStarter.class);
    }
}