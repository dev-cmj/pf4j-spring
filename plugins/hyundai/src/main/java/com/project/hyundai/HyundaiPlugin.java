package com.project.hyundai;

import org.laxture.sbp.SpringBootPlugin;
import org.laxture.sbp.spring.boot.SbpThymeleafConfigurer;
import org.laxture.sbp.spring.boot.SpringBootstrap;
import org.pf4j.PluginWrapper;

public class HyundaiPlugin extends SpringBootPlugin {

    public HyundaiPlugin(PluginWrapper wrapper) {
        super(wrapper, new SbpThymeleafConfigurer());
    }

    @Override
    protected SpringBootstrap createSpringBootstrap() {
        return new SpringBootstrap(this, HyundaiStarter.class);
    }
}