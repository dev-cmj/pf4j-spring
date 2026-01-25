package com.project.kcredit;

import com.project.core.extension.LoginExtension;
import lombok.extern.slf4j.Slf4j;
import org.pf4j.Extension;

@Slf4j
@Extension // 핵심! 이 어노테이션이 있어야 core가 이 클래스를 찾아냅니다.
public class KcreditLoginExtension implements LoginExtension {

    @Override
    public boolean preLogin(String userId, String remoteIp) {
        return false;
    }

    @Override
    public void onLoginSuccess(String userId) {

    }

    @Override
    public String getCustomErrorMessage() {
        return "";
    }
}