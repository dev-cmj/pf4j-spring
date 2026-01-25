package com.project.core.extension;

import org.pf4j.ExtensionPoint;

public interface LoginExtension extends ExtensionPoint {
    /**
     * 로그인 시도 직전 체크 (예: 특정 IP 차단, 접속 시간 제한 등)
     */
    boolean preLogin(String userId, String remoteIp);

    /**
     * 로그인 성공 직후 처리 (예: 사내 메신저 알림, 타 시스템 세션 동기화)
     */
    void onLoginSuccess(String userId);

    /**
     * 플러그인에서 제공하는 커스텀 에러 메시지
     */
    String getCustomErrorMessage();
}