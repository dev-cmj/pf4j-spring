package com.project.kcredit;

import org.pf4j.Plugin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KcreditPlugin extends Plugin {

    public KcreditPlugin() {
        super();
    }

    @Override
    public void start() {
        log.info(">>> Kcredit 전용 플러그인 활성화됨 (PF4J 3.x+ 표준)");
    }

    @Override
    public void stop() {
        log.info(">>> Kcredit 전용 플러그인 비활성화됨");
    }
}